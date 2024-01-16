package com.love.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.PostsDTO;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.PostsVO;
import com.love.product.entity.dto.PostsSearchDTO;
import com.love.product.entity.vo.ConditionVO;
import com.love.product.entity.vo.PostsDetailVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsService extends IService<Posts> {

    Result<Posts> add(Long userId, PostsVO postsVO);

    ResultPage<PostsDetailVO> getPage(Long userId, PostsPageReq postsPageReq);
    ResultPage<PostsDetailVO> getPageByEs(PostsPageReq postsPageReq);

    Result<PostsDetailVO> getDetail(Long userId, Long id);

    Result<?> browse(Long userId,Long id);

    Map<Long, PostsDetailVO> listByIds(List<Long> postsIds);

    Result<?> update(PostsVO postsVO);

    Result<?> del(Long userId, Long id);
     Result<?> delete(Long userId,Long id);
    String getImgPathById(Long id);

    List<PostsSearchDTO> listPostsBySearch(ConditionVO condition);

    void updatePostsCollectNum(Long postsId,Integer collectNum);
//    Result<List<Map<Long, String>>> listHot();
    Result<List<Posts>> listHot();

    Result<?> audit(PostsDTO postsDTO);

    Result<?> restore(Long userId, Long id);
}
