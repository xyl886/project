package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.PostsComment;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.PostsCommentVO;

import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsCommentService extends IService<PostsComment> {

    Result<?> add(Long userId,Long postsId,String content);

    Result<List<PostsCommentVO>> listByPostsId(Long postsId);

    Result<?> del(Long userId, Long id);

    Result<?> addCommentLike(Long userId, Long commentId, Integer deleted);
}
