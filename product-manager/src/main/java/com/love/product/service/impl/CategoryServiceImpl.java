package com.love.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.Exception.BizException;
import com.love.product.constant.CommonConstant;
import com.love.product.entity.Category;
import com.love.product.entity.base.Result;
import com.love.product.mapper.CategoryMapper;
import com.love.product.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static com.love.product.entity.base.ResultCode.CATEGORY_IS_EXIST;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: CategoryServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/13 14:58
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    /**
     * 获取分类
     * @return
     */
    @Override
    public Result<List<Category>> listAll() {
        return Result.OK(list());
    }

    /**
     * 获取分类name
     * @param id 分类id
     * @return categoryName 分类name
     */
    @Override
    public String getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        return category!=null? category.categoryName :"";
    }

    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>()
                .eq(CommonConstant.CATEGORYNAME, category.categoryName));
        if (ObjectUtil.isNull(vo)) {
            throw new BizException("该分类名称已存在!");
        }
        int rows = baseMapper.insert(category);
        return rows > 0 ? Result.OK("添加成功"): Result.failMsg("添加分类失败");
    }

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>()
                .eq(CommonConstant.CATEGORYNAME, category.categoryName));
        Assert.isTrue(!(vo != null && !vo.getId().equals(category.getId())),CATEGORY_IS_EXIST.getMsg());

        int rows = baseMapper.updateById(category);

        return rows > 0 ? Result.OK(): Result.failMsg("修改分类失败");
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCategory(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? Result.OK(): Result.failMsg("删除分类失败");
    }

    /**
     * 批量删除分类
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBatch(List<Category> list) {
        List<Long> ids = list.stream().map(Category::getId).collect(Collectors.toList());
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.OK(): Result.failMsg("批量删除分类失败");
    }
}
