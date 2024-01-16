package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.LikeCountDTO;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsLikeService extends IService<PostsLike> {

    /**
     * 点赞。状态为1 *
     *
     * @param likedUserId
     * @param likedPostId
     */
    void saveLikeToRedis(Long likedUserId, Long likedPostId);

    /**
     * 取消点赞。将状态改变为0
     *
     * @param likedUserId
     * @param likedPostId
     */
    void unlikeFromRedis(Long likedUserId, Long likedPostId);

    /**
     * 从Redis中删除一条点赞数据
     *
     * @param likedUserId
     * @param likedPostId
     */
    void deleteLikeFromRedis(Long likedUserId, Long likedPostId);

    /**
     * 该帖子的点赞数加1
     *
     * @param likedPostId
     */
    void incrementLikeCount(Long likedPostId);

    /**
     * 该帖子的点赞数减1
     *
     * @param likedPostId
     */
    void decrementLikeCount(Long likedPostId);

    /**
     * 获取Redis中存储的所有点赞数据
     *
     * @return
     */
    List<PostsLike> getLikeDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     *
     * @return
     */
    List<LikeCountDTO> getLikeCountFromRedis();

    Result<?> add(Long userId, Long postsId, Integer deleted);

    PostsLike getDetail(Long userId, Long postsId);

    Map<Long, PostsLike> listByUserId(Long userId, List<Long> postsIds);

    ResultPage<HistoryVO> getPage(Long userId, HistoryPageReq pageQuery);

    Result<?> like(Long postsId, Long userId);

    Result<?> dislike(Long infoId, Long userId);
}
