package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.Category;
import com.love.product.entity.dto.CategoryPostCountDTO;
import com.love.product.entity.req.CommentPageReq;
import com.love.product.entity.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PackageName: com.love.product.mapper
 * @Description: CategoryMapper
 * @Author: Administrator
 * @Date: 2023/7/13 14:56
 */

public interface CategoryMapper extends BaseMapper<Category> {
    List<CategoryPostCountDTO> getCategoryPostCount();
    Page<CategoryVO> getCategoryList(@Param("page") Page<Object> objectPage,
                                     @Param("categoryName") String categoryName);

    Integer getPostCountById(@Param("id") Integer id);
}
