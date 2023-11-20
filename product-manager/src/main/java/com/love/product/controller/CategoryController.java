package com.love.product.controller;

import com.love.product.entity.Category;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.entity.req.CategoryPageReq;
import com.love.product.entity.vo.CategoryVO;
import com.love.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller
 * @Description: CategoryController
 * @Author: Administrator
 * @Date: 2023/7/13 15:07
 */
@Api("分类")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/listAll")
    @ApiOperation(value = "分类列表", httpMethod = "POST", response = Result.class, notes = "分类列表")
    public ResultPage<CategoryVO> listAll(@RequestBody CategoryPageReq categoryPageReq){
        return categoryService.listAll(categoryPageReq);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiOperation(value = "分类详情", httpMethod = "GET", response = Result.class, notes = "分类详情")
    public String getCategoryById(@RequestParam() Long id){
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "新增分类", httpMethod = "POST", response = Result.class, notes = "新增分类")
    public Result insertCategory(@RequestBody Category category){
        return categoryService.insertCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改分类", httpMethod = "POST", response = Result.class, notes = "修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(categoryDTO);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除分类", httpMethod = "DELETE", response = Result.class, notes = "删除分类")
    public Result deleteCategory(Integer id){
        return categoryService.deleteCategory(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除分类", httpMethod = "DELETE", response = Result.class, notes = "批量删除分类")
    public Result deleteBatch(@RequestBody List<Category> list){
        return categoryService.deleteBatch(list);
    }


}
