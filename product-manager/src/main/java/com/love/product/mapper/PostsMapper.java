package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Posts;
import com.love.product.entity.dto.PostsDTO;
import com.love.product.entity.vo.ContributeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface PostsMapper extends BaseMapper<Posts> {
    List<Posts> listPostWithDelete(Date minUpdateTime);
    String getImgPathById(Long id);

    Posts getPostsById(Long postsId);

    /**
     * 文章贡献度
     *
     * @param lastTime 开始时间
     * @param nowTime  结束时间
     * @return
     */
    List<ContributeVO> contribute(@Param("lastTime") String lastTime, @Param("nowTime") String nowTime);

    void audit(@Param("postsDTO") PostsDTO postsDTO);
}
