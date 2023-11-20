package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Category;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.entity.dto.CategoryPostCountDTO;
import com.love.product.entity.req.CategoryPageReq;
import com.love.product.entity.vo.CategoryVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: CategoryService
 * @Author: Administrator
 * @Date: 2023/7/13 14:58
 */

public interface CategoryService extends IService<Category> {
    List<CategoryPostCountDTO> getCategoryPostCount();

    ResultPage<CategoryVO> listAll(CategoryPageReq categoryPageReq);

    String getCategoryById(Long id);


    Result insertCategory(Category category);


    Result updateCategory(CategoryDTO category);


    Result deleteCategory(Integer id);


    Result deleteBatch(List<Category> list);
}
