package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.LikeCountDTO;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.enums.LikeStatus;
import com.love.product.enums.YesOrNo;
import com.love.product.mapper.PostsLikeMapper;
import com.love.product.mapper.PostsMapper;
import com.love.product.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.love.product.constant.RedisKeyConstant.*;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostsLikeServiceImpl extends ServiceImpl<PostsLikeMapper, PostsLike> implements PostsLikeService {
    @Resource
    private PostsService postsService;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private PostsLikeMapper postsLikeMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private FileUploadService fileUploadService;

    public Integer getLikeStatus(Long postId, Long likeUserId) {
        if (redisService.hHasKey(MAP_KEY_USER_LIKED, getLikedKey(postId, likeUserId))){
            String hashKey = getLikedKey(likeUserId, postId);
            return (Integer) redisService.hGet(MAP_KEY_USER_LIKED, hashKey);
        }
        return LikeStatus.NOT_EXIST.getCode();
    }

    @Override
    public void saveLikeToRedis(Long likedUserId, Long likedPostId) {
        String hashKey = getLikedKey(likedUserId, likedPostId);
        redisService.hSet(MAP_KEY_USER_LIKED, hashKey,LikeStatus.LIKE.setHashValue());
    }

    @Override
    public void unlikeFromRedis(Long likedUserId, Long likedPostId) {
        String hashKey = getLikedKey(likedUserId, likedPostId);
        redisService.hSet(MAP_KEY_USER_LIKED, hashKey, LikeStatus.UNLIKE.setHashValue());
    }

    @Override
    public void deleteLikeFromRedis(Long likedUserId, Long likedPostId) {
        String hashKey = getLikedKey(likedUserId, likedPostId);
        redisService.hDel(MAP_KEY_USER_LIKED, hashKey);
    }

    @Override
    public void incrementLikeCount(Long likedPostId) {
        redisService.hIncr(MAP_KEY_USER_LIKED_COUNT, likedPostId, 1);
    }

    @Override
    public void decrementLikeCount(Long likedPostId) {
        redisService.hIncr(MAP_KEY_USER_LIKED_COUNT, likedPostId, -1);
    }

    @Override
    public List<PostsLike> getLikeDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<PostsLike> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            Long likedUserId = Long.valueOf(split[0]);
            Long likedPostId = Long.valueOf(split[1]);
            HashMap<String, Object> map = (HashMap<String, Object>) entry.getValue();
            Integer status = (Integer) map.get("status");
            PostsLike postsLike = new PostsLike(likedUserId, likedPostId,null, status);
            list.add(postsLike);
            redisService.hDel(MAP_KEY_USER_LIKED, key);
        }
        return list;
    }

    @Override
    public List<LikeCountDTO> getLikeCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikeCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            Long key = (Long) map.getKey();
            LikeCountDTO dto = new LikeCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            redisService.hDel(MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }

    @Override
    public Result<?> add(Long userId, Long postsId, Integer deleted) {
        Posts posts = postsService.getById(postsId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if (yesOrNo == null) {
            yesOrNo = YesOrNo.NO;
        }
        if (posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())) {
            LocalDateTime now = LocalDateTime.now();

            String key = getLikedKey(userId, postsId);
            redisService.hSet(MAP_KEY_USER_LIKED, key, LikeStatus.LIKE.getCode());

            // 发送点赞消息到RabbitMQ
            Map<String, Object> Rmsg = new HashMap<>();
            Rmsg.put("userId", userId);
            Rmsg.put("postsId", postsId);
            Rmsg.put("deleted", deleted);
            rabbitTemplate.convertAndSend("posts_like", Rmsg);

            PostsLike postsLike = new PostsLike();
            postsLike.setId(IdWorker.getId());
            postsLike.setUserId(userId);
            postsLike.setPostsId(posts.getId());
            postsLike.setPostsUserId(posts.getUserId());
            postsLike.setDeleted(yesOrNo.getValue());
            postsLike.setCreateTime(now);
            postsLike.setUpdateTime(now);
            postsLikeMapper.add(postsLike);
            String msg = "点赞成功";
            int likeNum = posts.getLikeNum();
            if (yesOrNo.equals(YesOrNo.YES)) {
                likeNum = likeNum - 1;
                postsService.saveOrUpdate(posts);
                msg = "已取消点赞";
            } else {
                likeNum = likeNum + 1;
            }
            if (likeNum < 0) {
                likeNum = 0;
            }
            posts.setLikeNum(likeNum);
            //更新点赞数
            postsService.saveOrUpdate(posts);
            return Result.OKMsg(msg);
        } else {
            return Result.failMsg("帖子不存在或已下架");
        }
    }

    @Override
    public PostsLike getDetail(Long userId, Long postsId) {
        LambdaQueryWrapper<PostsLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsLike::getUserId, userId)
                .eq(PostsLike::getPostsId, postsId)
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public Map<Long, PostsLike> listByUserId(Long userId, List<Long> postsIds) {
        Map<Long, PostsLike> postsLikeHashMap = new HashMap<>();
        if (userId == null || postsIds.size() == 0) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<PostsLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsLike::getUserId, userId);
        queryWrapper.in(PostsLike::getPostsId, postsIds);
        List<PostsLike> likes = list(queryWrapper);
        likes.forEach(item -> {
            postsLikeHashMap.put(item.getPostsId(), item);
        });
        return postsLikeHashMap;
    }

    @Override
    public ResultPage<HistoryVO> getPage(Long userId, HistoryPageReq pageQuery) {
        Page<HistoryVO> Page = baseMapper.selectPageList(
                new Page<>(pageQuery.getCurrentPage(), pageQuery.getPageSize()), pageQuery, userId);
        List<HistoryVO> list = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (Page.getTotal() > 0) {
            list = Page.getRecords().stream().peek(historyVO ->
                    postsIds.add(historyVO.getPostsId())
            ).collect(Collectors.toList());
        }
        // 获取帖子信息
        getHVOPosts(list, postsIds, postsService, categoryService);
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }

    @Override
    public Result<?> like(Long postId, Long userId) {
            // 查询Redis是否已经存储为喜欢
            Integer status = this.getLikeStatus(postId, userId);
            if (Objects.equals(status, LikeStatus.LIKE.getCode())){// 已经存在喜欢
                return Result.failMsg("已经点赞该内容啦，请勿重复点赞！");
            }
            // 不存在或者之前是取消喜欢
            try {
                this.saveLikeToRedis(postId, userId);
                this.incrementLikeCount(postId);
                return Result.OK("点赞成功！");
            }catch (Exception e){
                e.printStackTrace();
                return Result.failMsg("点赞失败，请稍后重试！");
            }

    }

    @Override
    public Result<?> dislike(Long postId, Long userId) {
        // 查询Redis是否已经存储为取消喜欢
        Integer status = getLikeStatus(postId, userId);
        if (Objects.equals(status, LikeStatus.UNLIKE.getCode())){// 已经存在取消喜欢
            return Result.failMsg("已经取消点赞该内容啦，请勿重复取消点赞！");
        }
        else if (Objects.equals(status, LikeStatus.NOT_EXIST.getCode())) {// 不存在取消喜欢，修改状态，增加0条
            unlikeFromRedis(postId, userId);
            this.decrementLikeCount(postId);
            return Result.OK("取消点赞成功！");
        }
        else{// 之前已经喜欢，则修改状态,并喜欢数-1
            try {
                unlikeFromRedis(postId, userId);
                this.decrementLikeCount(postId);
                return Result.OK("取消点赞成功！");
            }catch (Exception e){
                e.printStackTrace();
                return Result.failMsg("取消点赞失败，请稍后重试！");
            }
        }
    }

    public PostsLike getByInfoIdAndLikeUserId (Long postId, Long likeUserId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("postsId",postId);
        map.put("userId",likeUserId);
        try{
            PostsLike postsLike = baseMapper.selectByMap(map).get(0);
            log.info("通过被点赞人和点赞人id查询是否存在点赞记录");
            return postsLike;
        }catch (Exception e){
            log.info("当前查询的被点赞人和点赞人id不存在点赞记录");
            return null;
        }
    }
    public Boolean update(PostsLike postsLike) {
        UpdateWrapper<PostsLike> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", postsLike.getStatus());
        updateWrapper.set("update_time",postsLike.getUpdateTime());
        updateWrapper.eq("id",postsLike.getId());

        int rows = postsLikeMapper.update(postsLike,updateWrapper);
        return rows > 0;
    }
    public void transLikeFromRedisToMysql() {
        // 批量获取缓存中的点赞数据
        List<PostsLike> list = this.getLikeDataFromRedis();
        if (CollectionUtils.isEmpty(list))// 为空，不写入
            return;
        for (PostsLike item: list){
            PostsLike postsLike = this.getByInfoIdAndLikeUserId(item.getPostsId(), item.getUserId());// 在数据库中查询
            if (postsLike == null) {// 无记录，新增
                if(!save(item)){
                    log.info("新增点赞数据失败！");
                    return;
                }
            }else {// 有记录，更新
                // 判断数据库中点赞状态与缓存中点赞状态一致性
                if (Objects.equals(postsLike.getStatus(), item.getStatus())){// 一致，无需持久化，点赞数量-1
                    incrementLikeCount(item.getPostsId());
                }else{// 不一致
                    if (Objects.equals(postsLike.getStatus(), LikeStatus.LIKE.getCode())){// 在数据库中已经是点赞，则取消点赞，同时记得redis中的count-1
                        // 之前是点赞，现在改为取消点赞 1.设置更改status 2. redis中的count要-1（消除在数据库中自己的记录）
                        postsLike.setStatus(LikeStatus.UNLIKE.getCode());
                        this.decrementLikeCount(item.getPostsId());
                    } else if (Objects.equals(postsLike.getStatus(), LikeStatus.UNLIKE.getCode())) {// 未点赞，则点赞，修改点赞状态和点赞数据+1
                        postsLike.setStatus(LikeStatus.LIKE.getCode());
                        this.incrementLikeCount(item.getPostsId());
                    }
                    postsLike.setUpdateTime(LocalDateTime.now());
                    if(!update(postsLike)){// 更新点赞数据
                        log.info("更新点赞数据失败！");
                        return;
                        // System.out.println("缓存记录更新数据库失败！请重试");
                    }
                }
            }
        }
    }

    public void transLikeCountFromRedisToMysql() {
// 获取缓存中内容的点赞数列表
        List<LikeCountDTO> list = this.getLikeCountFromRedis();
        if (CollectionUtils.isEmpty(list))// 为空，不写入
            return;
        for (LikeCountDTO item: list){
            Posts posts = postsMapper.selectById(item.getPostId());
            if (posts != null) {// 新增点赞数
                Integer likeCount = posts.getLikeNum() + item.getCount();
                System.out.println("内容id不为空，更新内容点赞数量");
                posts.setLikeNum(likeCount);

                UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("like_count", posts.getLikeNum());
                updateWrapper.eq("id", posts.getId());
                int rows = postsMapper.update(posts, updateWrapper);
                if (rows > 0) {
                    System.out.println("成功更新内容点赞数！");
                    return;
                }
            }
            System.out.println("内容id不存在，无法将缓存数据持久化！");
        }
    }
    static void getHVOPosts(List<HistoryVO> list, List<Long> postsIds, PostsService postsService, CategoryService categoryService) {
        Map<Long, PostsDetailVO> postsHashMap;
        if (!postsIds.isEmpty()) {
            postsHashMap = postsService.listByIds(postsIds);
            for (HistoryVO historyVO : list) {
                PostsDetailVO postsDetailVO = postsHashMap.get(historyVO.getPostsId());
                // 设置帖子标题和封面路径
                if (postsDetailVO != null) {
                    historyVO.setPostTitle(postsDetailVO.getTitle());
                    historyVO.setPostCoverPath(postsDetailVO.getCoverPath());
                    historyVO.setPostType(postsDetailVO.getPostsType());
                    historyVO.setPosts(postsDetailVO);
                    historyVO.setSchoolName(
                            categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
                }
            }
        }
    }
}
