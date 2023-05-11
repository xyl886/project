package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Collect;
import com.love.product.entity.PostsLike;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsLikeMapper extends BaseMapper<PostsLike> {

    int add(PostsLike postsLike);

}
