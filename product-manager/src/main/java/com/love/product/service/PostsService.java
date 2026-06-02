package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Posts;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.PostsDTO;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.PostsVO;
import com.love.product.entity.vo.ConditionVO;
import com.love.product.entity.vo.PostsDetailVO;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsService extends IService<Posts> {

    Posts add(PostsVO postsVO);

    ResultPage<PostsDetailVO> getPage(PostsPageReq postsPageReq);

    PostsDetailVO getDetail(Long id);

    void browse(Long userId, Long id);

    Map<Long, PostsDetailVO> listByIds(List<Long> postsIds);

    void update(PostsVO postsVO);

    void del(Long userId, Long id);

    void delete(Long userId, Long id);

    String getImgPathById(Long id);

    List<Posts> listPostsBySearch(ConditionVO condition);

    void updatePostsCollectNum(Long postsId, Integer collectNum);

    List<PostsDetailVO> listHot();

    void audit(PostsDTO postsDTO);

    void restore(Long userId, Long id);

    List<PostsDetailVO> getUserPosts(Long userId, Integer page, Integer size);
}
