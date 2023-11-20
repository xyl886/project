package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.PostsComment;
import com.love.product.entity.req.CommentPageReq;
import com.love.product.entity.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsCommentMapper extends BaseMapper<PostsComment> {

    Page<CommentVO> selectPageList(
            @Param("page")Page<Object> objectPage,
            @Param("pageQuery") CommentPageReq CommentPageReq);
}
