package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.model.VO.PostsVO;
import com.love.product.model.DTO.PostsSearchDTO;
import com.love.product.model.VO.ConditionVO;
import com.love.product.model.VO.PostsDetailVO;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsService extends IService<Posts> {

    Result<Posts> add(Long userId, PostsVO postsVO);

    ResultPage<PostsDetailVO> getPage(Long userId, PostsPageReq postsPageReq);

    Result<PostsDetailVO> getDetail(Long userId, Long id);

    Result<?> browse(Long userId,Long id);

    Map<Long, PostsDetailVO> listByIds(List<Long> postsIds);

    Result<?> update(PostsVO postsVO);

    Result<?> del(Long userId, Long id);

    String getImgPathById(Long id);

    List<PostsSearchDTO> listPostsBySearch(ConditionVO condition);
}
