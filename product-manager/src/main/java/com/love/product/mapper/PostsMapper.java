package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Posts;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsMapper extends BaseMapper<Posts> {
String getImgPathById( Long id);

    Posts getPostsById(Long postsId);
}
