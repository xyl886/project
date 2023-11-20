package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.Exception.BizException;
import com.love.product.constant.CommonConstant;
import com.love.product.entity.Category;
import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.entity.dto.CategoryPostCountDTO;
import com.love.product.entity.req.CategoryPageReq;
import com.love.product.entity.vo.CategoryVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.enumerate.PostStatus;
import com.love.product.enumerate.PostsType;
import com.love.product.mapper.CategoryMapper;
import com.love.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public List<CategoryPostCountDTO> getCategoryPostCount() {
        return categoryMapper.getCategoryPostCount();
    }
    /**
     * 获取分类
     * @return
     */
    @Override
    public ResultPage<CategoryVO> listAll(CategoryPageReq categoryPageReq) {
//        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(StringUtils.isNotBlank(categoryPageReq.getCategoryName()),
//                        Category::getCategoryName,categoryPageReq.getCategoryName())
//                .orderByAsc(Category::getCreateTime);
//        Page<Category> page=page(categoryPageReq.build(),lambdaQueryWrapper);
//        List<CategoryVO> list=new ArrayList<>();
//        if (page.getTotal() > 0) {
//            list = page.getRecords().stream().map(category -> {
//                CategoryVO categoryVO = BeanUtil.copyProperties(category, CategoryVO.class);
//                categoryVO.setPostCount(categoryMapper.getPostCountById(categoryVO.id));
//                return categoryVO;
//            }).collect(Collectors.toList());
//        }
        Page<CategoryVO> page=baseMapper.getCategoryList(
                new Page<>(categoryPageReq.getCurrentPage(), categoryPageReq.getPageSize()), categoryPageReq.getCategoryName());
        List<CategoryVO> list=new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords();
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
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
        log.info(String.valueOf(vo));
        if (!ObjectUtil.isNull(vo)) {
            throw new BizException("该分类名称已存在!");
        }
        int rows = baseMapper.insert(category);
        return rows > 0 ? Result.OK("添加成功"): Result.failMsg("添加分类失败");
    }

    /**
     * 修改分类or增加分类
     * @param categoryDTO 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCategory(CategoryDTO categoryDTO) {
        if(categoryDTO.getId()== null){
            Category vo = baseMapper.selectOne(new QueryWrapper<Category>()
                    .eq(CommonConstant.CATEGORYNAME, categoryDTO.getCategoryName()));
            log.info(String.valueOf(vo));
            if (!ObjectUtil.isNull(vo)) {
               return  Result.failMsg(CATEGORY_IS_EXIST.getMsg());
            }
            Category category = new Category();
            BeanUtils.copyProperties(categoryDTO,category);
            int rows = baseMapper.insert(category);
            return rows > 0 ? Result.OK(): Result.failMsg("添加分类失败");
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
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
    public Result deleteCategory(Integer id) {
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
        List<Integer> ids = list.stream().map(Category::getId).collect(Collectors.toList());
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.OK(): Result.failMsg("批量删除分类失败");
    }
}
