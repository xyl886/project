package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.req.PostsReq;
import com.love.product.entity.vo.PostsVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsService extends IService<Posts> {

    Result<Posts> add(Long userId, PostsReq postsReq) throws IOException;

    ResultPage<PostsVO> getPage(Long userId,PostsPageReq postsPageReq);

    Result<PostsVO> getDetail(Long userId,Long id);

    Result<?> browse(Long userId,Long id);

    Map<Long, PostsVO> listByIds(List<Long> postsIds);

    Result<?> update(Long id, PostsReq postsReq, String title, String content, String school);

    Result<?> del(Long userId, Long id);

}
