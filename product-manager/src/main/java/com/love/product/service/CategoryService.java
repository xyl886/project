package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Category;
import com.love.product.entity.base.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: CategoryService
 * @Author: Administrator
 * @Date: 2023/7/13 14:58
 */

public interface CategoryService extends IService<Category> {
    Result<List<Category>> listAll();

    String getCategoryById(Long id);


    Result insertCategory(Category category);


    Result updateCategory(Category category);


    Result deleteCategory(Long id);


    Result deleteBatch(List<Category> list);
}
