package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    Result listCategory(String name);

    /**
     * 分类详情
     * @param id 分类id
     * @return
     */
    Result getCategoryById(Long id);

    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    Result insertCategory(Category category);

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    Result updateCategory(Category category);

    /**
     * 删除分类
     * @param id 分类id
     * @return
     */
    Result deleteCategory(Long id);

    /**
     * 批量删除分类
     * @param list 分类对象集合
     * @return
     */
    Result deleteBatch(List<Category> list);


    /**
     * 置顶分类
     * @return 置顶分类
     */
    Result top(Long id);


    //-----------web端方法开始------------------
    /**
     * 首页分类列表
     * @return
     */
    Result webList();

}
