package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import static com.love.product.constant.RedisKeyConstant.LIKE_COUNT;
import static com.love.product.constant.RedisKeyConstant.LIKE_STATUS;


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

    public Integer getLikeStatus(Long postId, Long userId) {
        String hashKey = userId + ":" + postId;
        if (redisService.hHasKey(LIKE_STATUS, hashKey)) {
            Object val = redisService.hGet(LIKE_STATUS, hashKey);
            if (val instanceof Integer) return (Integer) val;
            if (val instanceof HashMap) {
                HashMap map = (HashMap) val;
                return map.containsKey("status") ? (Integer) map.get("status") : LikeStatus.NOT_EXIST.getCode();
            }
        }
        PostsLike dbLike = getDetail(userId, postId);
        if (dbLike != null) return dbLike.getDeleted();
        return LikeStatus.NOT_EXIST.getCode();
    }

    @Override
    public void saveLikeToRedis(Long likedUserId, Long likedPostId) {
        redisService.hSet(LIKE_STATUS, likedUserId + ":" + likedPostId, LikeStatus.LIKE.getCode());
    }

    @Override
    public void unlikeFromRedis(Long likedUserId, Long likedPostId) {
        redisService.hSet(LIKE_STATUS, likedUserId + ":" + likedPostId, LikeStatus.UNLIKE.getCode());
    }

    @Override
    public void deleteLikeFromRedis(Long likedUserId, Long likedPostId) {
        redisService.hDel(LIKE_STATUS, likedUserId + ":" + likedPostId);
    }

    @Override
    public void incrementLikeCount(Long likedPostId) {
        redisService.hIncr(LIKE_COUNT, likedPostId.toString(), 1L);
    }

    @Override
    public void decrementLikeCount(Long likedPostId) {
        redisService.hIncr(LIKE_COUNT, likedPostId.toString(), -1L);
    }

    @Override
    public List<PostsLike> getLikeDataFromRedis() {
        List<PostsLike> list;
        try (Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(LIKE_STATUS, ScanOptions.NONE)) {
            list = new ArrayList<>();
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String key = (String) entry.getKey();
                String[] split = key.split(":");
                Long likedUserId = Long.valueOf(split[0]);
                Long likedPostId = Long.valueOf(split[1]);
                Integer status = (Integer) entry.getValue();
                PostsLike postsLike = new PostsLike();
                postsLike.setUserId(likedUserId);
                postsLike.setPostsId(likedPostId);
                postsLike.setDeleted(status);
                list.add(postsLike);
                redisService.hDel(LIKE_STATUS, key);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<LikeCountDTO> getLikeCountFromRedis() {
        List<LikeCountDTO> list;
        try (Cursor<Map.Entry<Object, Object>> cursor = redisService.scan(LIKE_COUNT, ScanOptions.NONE)) {
            list = new ArrayList<>();
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                String key = (String) map.getKey();
                Long postId = Long.parseLong(key);
                LikeCountDTO dto = new LikeCountDTO(postId, (Integer) map.getValue());
                list.add(dto);
                redisService.hDel(LIKE_COUNT, key);
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
        if (yesOrNo == null) yesOrNo = YesOrNo.NO;
        if (posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())) {
            LocalDateTime now = LocalDateTime.now();
            String key = userId + ":" + postsId;
            boolean isLike = !yesOrNo.equals(YesOrNo.YES);
            redisService.hSet(LIKE_STATUS, key, isLike ? LikeStatus.LIKE.getCode() : LikeStatus.UNLIKE.getCode());
            if (isLike) incrementLikeCount(postsId);
            else decrementLikeCount(postsId);

            PostsLike postsLike = new PostsLike();
            postsLike.setId(IdWorker.getId());
            postsLike.setUserId(userId);
            postsLike.setPostsId(posts.getId());
            postsLike.setDeleted(yesOrNo.getValue());
            postsLike.setCreateTime(now);
            postsLike.setUpdateTime(now);
            postsLikeMapper.add(postsLike);

            int likeNum = posts.getLikeNum();
            likeNum = isLike ? likeNum + 1 : likeNum - 1;
            if (likeNum < 0) likeNum = 0;
            postsMapper.updateLikeNum(postsId, likeNum);
            return Result.OKMsg(isLike ? "点赞成功" : "已取消点赞");
        }
        return Result.failMsg("帖子不存在或已下架");
    }

    @Override
    public PostsLike getDetail(Long userId, Long postsId) {
        return getOne(new LambdaQueryWrapper<PostsLike>()
                .eq(PostsLike::getUserId, userId)
                .eq(PostsLike::getPostsId, postsId).last("LIMIT 1"));
    }

    @Override
    public Map<Long, PostsLike> listByUserId(Long userId, List<Long> postsIds) {
        Map<Long, PostsLike> map = new HashMap<>();
        if (userId == null || postsIds.isEmpty()) return map;
        list(new LambdaQueryWrapper<PostsLike>().eq(PostsLike::getUserId, userId).in(PostsLike::getPostsId, postsIds))
                .forEach(l -> map.put(l.getPostsId(), l));
        return map;
    }

    @Override
    public ResultPage<HistoryVO> getPage(Long userId, HistoryPageReq pageQuery) {
        Page<HistoryVO> Page = baseMapper.selectPageList(
                new Page<>(pageQuery.getCurrentPage(), pageQuery.getPageSize()), pageQuery, userId);
        List<HistoryVO> list = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (Page.getTotal() > 0) {
            list = Page.getRecords().stream().peek(h -> postsIds.add(h.getPostsId())).collect(Collectors.toList());
        }
        getHVOPosts(list, postsIds);
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }

    @Override
    public Result<?> like(Long postId, Long userId) {
        Integer status = getLikeStatus(postId, userId);
        if (Objects.equals(status, LikeStatus.LIKE.getCode()))
            return Result.failMsg("已经点赞该内容啦，请勿重复点赞！");
        saveLikeToRedis(userId, postId);
        incrementLikeCount(postId);
        PostsLike dbLike = getDetail(userId, postId);
        if (dbLike != null) {
            dbLike.setDeleted(LikeStatus.LIKE.getCode());
            updateById(dbLike);
        } else {
            PostsLike nl = new PostsLike();
            nl.setId(IdWorker.getId());
            nl.setUserId(userId);
            nl.setPostsId(postId);
            nl.setDeleted(LikeStatus.LIKE.getCode());
            nl.setCreateTime(LocalDateTime.now());
            save(nl);
        }
        return Result.OKMsg("点赞成功！");
    }

    @Override
    public Result<?> dislike(Long postId, Long userId) {
        Integer status = getLikeStatus(postId, userId);
        if (Objects.equals(status, LikeStatus.UNLIKE.getCode()))
            return Result.failMsg("已经取消点赞该内容啦，请勿重复取消点赞！");
        unlikeFromRedis(userId, postId);
        decrementLikeCount(postId);
        PostsLike dbLike = getDetail(userId, postId);
        if (dbLike != null) {
            dbLike.setDeleted(LikeStatus.UNLIKE.getCode());
            dbLike.setUpdateTime(LocalDateTime.now());
            updateById(dbLike);
        } else {
            PostsLike nl = new PostsLike();
            nl.setId(IdWorker.getId());
            nl.setUserId(userId);
            nl.setPostsId(postId);
            nl.setDeleted(LikeStatus.UNLIKE.getCode());
            nl.setCreateTime(LocalDateTime.now());
            save(nl);
        }
        return Result.OKMsg("取消点赞成功！");
    }

    public void initLikeFromMysqlToRedis() {
        log.info("initLikeFromMysqlToRedis 方法暂未实现");
    }

    @Override
    public void transLikeFromRedisToMysql() {
        List<PostsLike> list = getLikeDataFromRedis();
        if (list == null || list.isEmpty()) return;
        for (PostsLike item : list) {
            PostsLike postsLike = getDetail(item.getUserId(), item.getPostsId());
            if (postsLike == null) save(item);
            else {
                postsLike.setDeleted(item.getDeleted());
                postsLike.setUpdateTime(LocalDateTime.now());
                updateById(postsLike);
            }
        }
    }

    @Override
    public void transLikeCountFromRedisToMysql() {
        List<LikeCountDTO> list = getLikeCountFromRedis();
        if (list == null || list.isEmpty()) return;
        for (LikeCountDTO item : list) {
            Posts posts = postsMapper.selectById(item.getPostId());
            if (posts != null) {
                posts.setLikeNum(posts.getLikeNum() + item.getCount());
                postsMapper.updateById(posts);
            }
        }
    }

    @Override
    public void getHVOPosts(List<HistoryVO> list, List<Long> postsIds) {
        if (postsIds.isEmpty()) return;
        Map<Long, PostsDetailVO> postsHashMap = postsService.listByIds(postsIds);
        for (HistoryVO h : list) {
            PostsDetailVO p = postsHashMap.get(h.getPostsId());
            if (p != null) {
                h.setPostTitle(p.getTitle());
                h.setPostCoverPath(p.getCoverPath());
                h.setPostType(p.getPostsType());
                h.setPosts(p);
                h.setSchoolName(categoryService.getCategoryById(Long.valueOf(p.getCategoryId())));
            }
        }
    }
}
