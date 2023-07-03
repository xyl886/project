package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.Result;
import com.shiyi.entity.Category;
import com.shiyi.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客分类表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/system/category")
@RequiredArgsConstructor
@Api(tags = "分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "分类列表", httpMethod = "GET", response = Result.class, notes = "分类列表")
    public Result list(String name){
        return categoryService.listCategory(name);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @SaCheckPermission("/system/category/info")
    @ApiOperation(value = "分类详情", httpMethod = "GET", response = Result.class, notes = "分类详情")
    public Result getCategoryById(@RequestParam() Long id){
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("/system/category/add")
    @ApiOperation(value = "新增分类", httpMethod = "POST", response = Result.class, notes = "新增分类")
    @OperationLogger(value = "新增分类")
    public Result insertCategory(@RequestBody Category category){
        return categoryService.insertCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SaCheckPermission("/system/category/update")
    @ApiOperation(value = "修改分类", httpMethod = "POST", response = Result.class, notes = "修改分类")
    @OperationLogger(value = "修改分类")
    public Result update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/category/delete")
    @ApiOperation(value = "删除分类", httpMethod = "DELETE", response = Result.class, notes = "删除分类")
    @OperationLogger(value = "删除分类")
    public Result deleteCategory(Long id){
        return categoryService.deleteCategory(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/category/deleteBatch")
    @ApiOperation(value = "批量删除分类", httpMethod = "DELETE", response = Result.class, notes = "批量删除分类")
    @OperationLogger(value = "批量删除分类")
    public Result deleteBatch(@RequestBody List<Category> list){
        return categoryService.deleteBatch(list);
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @SaCheckPermission("/system/category/top")
    @ApiOperation(value = "置顶分类", httpMethod = "GET", response = Result.class, notes = "置顶分类")
    @OperationLogger(value = "置顶分类")
    public Result top(Long id){
        return categoryService.top(id);
    }
}

