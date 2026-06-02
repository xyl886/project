package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.PostsComment;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CommentPageReq;
import com.love.product.entity.vo.CommentVO;
import com.love.product.entity.vo.PostsCommentVO;

import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsCommentService extends IService<PostsComment> {

    void add(Long userId, Long postsId, String content, Long parentCommentId);

    List<PostsCommentVO> listByPostsId(Long postsId);

    void del(Long userId, Long id);

    void addCommentLike(Long userId, Long commentId, Integer deleted);

    void deleteBatch(List<Long> ids);

    ResultPage<CommentVO> listComment(CommentPageReq commentPageReq);

    void deleteComment(Long id);
}
