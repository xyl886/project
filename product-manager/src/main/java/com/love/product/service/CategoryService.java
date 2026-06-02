package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Category;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.entity.req.CategoryPageReq;
import com.love.product.entity.vo.CategoryVO;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: CategoryService
 * @Author: Administrator
 * @Date: 2023/7/13 14:58
 */

public interface CategoryService extends IService<Category> {

    ResultPage<CategoryVO> listAll(CategoryPageReq categoryPageReq);

    String getCategoryById(Long id);

    void insertCategory(Category category);

    void updateCategory(CategoryDTO category);

    void deleteCategory(Integer id);

    void deleteBatch(List<Category> list);
}
