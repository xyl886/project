package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.PostsLike;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsLikeMapper extends BaseMapper<PostsLike> {

    int add(PostsLike postsLike);

    Page<HistoryVO> selectPageList(
            @Param("page")Page<Object> objectPage,
            @Param("pageQuery") HistoryPageReq HistoryPageReq,
            @Param("userId") Long userId);
}
