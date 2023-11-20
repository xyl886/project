package com.love.product.controller.system;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysCategoryController
 * @Author: Administrator
 * @Date: 2023/8/1 9:11
 */

import com.love.product.entity.Category;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/category")
public class SysCategoryController {
    @Resource
    private CategoryService categoryService;
    @PostMapping(value = "/update")
    @ApiOperation(value = "修改or新增分类", httpMethod = "POST", response = Result.class, notes = "修改文章")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }
    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除分类", httpMethod = "POST", response = Result.class, notes = "修改文章")
    public Result delete(Integer id) {
        return categoryService.deleteCategory(id);
    }
    @RequestMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除分类", httpMethod = "POST", response = Result.class, notes = "批量删除分类")
    public Result deleteBatch(@RequestBody List<Category> list){
        return categoryService.deleteBatch(list);
    }

}
