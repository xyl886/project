package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.Tags;
import com.love.product.entity.vo.TagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PackageName: com.love.product.mapper
 * @Description: TagsMapper
 * @Author: Administrator
 * @Date: 2023/5/23 19:25
 */

public interface TagsMapper extends BaseMapper<Tags> {
     String getTagNameById(Long tagId) ;

     void savePostsTags(@Param("posts_id")Long id, @Param("tagList")List<Long> tagList);

     List<TagVO> selectAll();
}
