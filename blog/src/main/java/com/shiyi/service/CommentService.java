package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.Comment;
import com.shiyi.dto.CommentDTO;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface CommentService extends IService<Comment> {

    Result listComment(String keywords);

    Result deleteBatch(List<Integer> ids);



//    ------web端方法开始------
    Result comments(Long articleId);

    Result addComment(CommentDTO comment);

    Result repliesByComId(Integer commentId);

}
