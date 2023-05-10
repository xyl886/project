package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;

import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 */
public interface PostsLikeService extends IService<PostsLike> {

    Result<?> add(Long userId, Long postsId, Integer deleted);

    PostsLike getDetail(Long userId, Long postsId);

    Map<Long, PostsLike> listByUserId(Long userId, List<Long> postsIds);
}
