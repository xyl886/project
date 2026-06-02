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
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    public Integer getLikeStatus(Long postId, Long likeUserId) {
        String hashKey = getLikedKey(likeUserId, postId);
        if (redisService.hHasKey(MAP_KEY_USER_LIKED, hashKey)) {
            return (Integer) redisService.hGet(MAP_KEY_USER_LIKED, hashKey);
        }
        return LikeStatus.NOT_EXIST.getCode();
    }

    @Override
    public void saveLikeToRedis(Long likedUserId, Long likedPostId) {
        String hashKey = getLikedKey(likedUserId, likedPostId);
        redisService.hSet(MAP_KEY_USER_LIKED, hashKey, LikeStatus.LIKE.setHashValue());
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
        redisService.hIncr(MAP_KEY_USER_LIKED_COUNT, likedPostId.toString(), 1L);
    }

    @Override
    public void decrementLikeCount(Long likedPostId) {
        redisService.hIncr(MAP_KEY_USER_LIKED_COUNT, likedPostId.toString(), -1L);
    }

    @Override
    public List<PostsLike> getLikeDataFromRedis() {
        List<PostsLike> list;
        try (Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(MAP_KEY_USER_LIKED, ScanOptions.NONE)) {
            list = new ArrayList<>();
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String key = (String) entry.getKey();
                String[] split = key.split("::");
                Long likedUserId = Long.valueOf(split[0]);
                Long likedPostId = Long.valueOf(split[1]);
                HashMap<String, Object> map = (HashMap<String, Object>) entry.getValue();
                Integer status = (Integer) map.get("status");
                PostsLike postsLike = new PostsLike();
                list.add(postsLike);
                redisService.hDel(MAP_KEY_USER_LIKED, key);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<LikeCountDTO> getLikeCountFromRedis() {
        List<LikeCountDTO> list;
        try (Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE)) {
            list = new ArrayList<>();
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                String key = (String) map.getKey();
                Long postId = Long.parseLong((key));
                LikeCountDTO dto = new LikeCountDTO(postId, (Integer) map.getValue());
                list.add(dto);
                redisService.hDel(MAP_KEY_USER_LIKED_COUNT, key);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Result<?> add(Long userId, Long postsId, Integer deleted) {
        Posts posts = postsMapper.selectById(postsId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if (yesOrNo == null) {
            yesOrNo = YesOrNo.NO;
        }
        if (posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())) {
            LocalDateTime now = LocalDateTime.now();

            // 更新 Redis 缓存
            String key = getLikedKey(userId, postsId);
            boolean isLike = !yesOrNo.equals(YesOrNo.YES);
            redisService.hSet(MAP_KEY_USER_LIKED, key, isLike ? LikeStatus.LIKE.getCode() : LikeStatus.UNLIKE.getCode());

            // 更新 Redis 点赞计数
            if (isLike) {
                incrementLikeCount(postsId);
            } else {
                decrementLikeCount(postsId);
            }

            // 直接写点赞记录到DB
            PostsLike postsLike = new PostsLike();
            postsLike.setId(IdWorker.getId());
            postsLike.setUserId(userId);
            postsLike.setPostsId(posts.getId());
            postsLike.setDeleted(yesOrNo.getValue());
            postsLike.setCreateTime(now);
            postsLike.setUpdateTime(now);
            postsLikeMapper.add(postsLike);

            // 只更新点赞数字段，避免加载整个帖子对象
            int likeNum = posts.getLikeNum();
            likeNum = isLike ? likeNum + 1 : likeNum - 1;
            if (likeNum < 0) likeNum = 0;
            postsMapper.updateLikeNum(postsId, likeNum);

            return Result.OKMsg(isLike ? "点赞成功" : "已取消点赞");
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
        likes.forEach(item -> postsLikeHashMap.put(item.getPostsId(), item));
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
        getHVOPosts(list, postsIds);
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }

    @Override
    public Result<?> like(Long postId, Long userId) {
        // 查询Redis是否已经存储为喜欢
        Integer status = this.getLikeStatus(postId, userId);
        if (Objects.equals(status, LikeStatus.LIKE.getCode())) {// 已经存在喜欢
            return Result.failMsg("已经点赞该内容啦，请勿重复点赞！");
        }
        // 不存在或者之前是取消喜欢
        try {
            this.saveLikeToRedis(userId, postId);
            this.incrementLikeCount(postId);
            return Result.OKMsg("点赞成功！");
        } catch (Exception e) {
            log.error("点赞失败", e);
            return Result.failMsg("点赞失败，请稍后重试！");
        }

    }

    @Override
    public Result<?> dislike(Long postId, Long userId) {
        // 查询Redis是否已经存储为取消喜欢
        Integer status = getLikeStatus(postId, userId);
        if (Objects.equals(status, LikeStatus.UNLIKE.getCode())) {// 已经存在取消喜欢
            return Result.failMsg("已经取消点赞该内容啦，请勿重复取消点赞！");
        } else if (Objects.equals(status, LikeStatus.NOT_EXIST.getCode())) {// 不存在取消喜欢，修改状态，增加0条
            unlikeFromRedis(userId, postId);
            this.decrementLikeCount(postId);
            return Result.OKMsg("取消点赞成功！");
        } else {// 之前已经喜欢，则修改状态,并喜欢数-1
            try {
                unlikeFromRedis(userId, postId);
                this.decrementLikeCount(postId);
                return Result.OKMsg("取消点赞成功！");
            } catch (Exception e) {
                log.error("取消点赞失败", e);
                return Result.failMsg("取消点赞失败，请稍后重试！");
            }
        }
    }

    public void initLikeFromMysqlToRedis() {
        log.info("initLikeFromMysqlToRedis 方法暂未实现");
    }

    @Override
    public void transLikeFromRedisToMysql() {
        // 批量获取缓存中的点赞数据并持久化到MySQL
        List<PostsLike> list = this.getLikeDataFromRedis();
        if (list == null || list.isEmpty())
            return;
        for (PostsLike item : list) {
            PostsLike postsLike = this.getDetail(item.userId, item.postsId);
            if (postsLike == null) {
                save(item);
            } else {
                // 更新点赞记录（deleted 字段表示点赞状态）
                postsLike.setDeleted(item.getDeleted());
                postsLike.setUpdateTime(LocalDateTime.now());
                updateById(postsLike);
            }
        }
    }

    @Override
    public void transLikeCountFromRedisToMysql() {
        // 获取缓存中内容的点赞数列表
        List<LikeCountDTO> list = this.getLikeCountFromRedis();
        log.info(list.toString());
        if (list == null || list.isEmpty())// 为空，不写入
            return;
        for (LikeCountDTO item : list) {
            Posts posts = postsMapper.selectById(item.postId);
            if (posts != null) {// 新增点赞数
                Integer likeCount = posts.likeNum + item.count;
                log.info("开始更新点赞数至posts，postId为" + posts.id);
                posts.setLikeNum(likeCount);
                UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("like_num", posts.likeNum);
                updateWrapper.eq("id", posts.id);
                int rows = postsMapper.update(posts, updateWrapper);
                if (rows > 0) {
                    log.info("成功更新点赞数至posts，postId为" + posts.id);
                    continue;
                }
            }
            log.info("内容id不存在，无法将缓存数据持久化！");
        }
    }

    @Override
    public void getHVOPosts(List<HistoryVO> list, List<Long> postsIds) {
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
                            categoryService.getCategoryById(Long.valueOf(postsDetailVO.categoryId)));
                }
            }
        }
    }
}
