package com.love.product.controller.system;

import com.love.product.entity.Tags;
import com.love.product.entity.base.Result;
import com.love.product.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: TagsController
 * @Author: Administrator
 * @Date: 2023/7/17 17:10
 */
@RestController
@RequestMapping("/system/tags")
@Api(tags = "标签管理")
public class TagsController {
    @Resource
    private TagsService tagsService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = Result.class, notes = "标签列表")
    public Result list(String name){
        return tagsService.listTags(name);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "新增标签", httpMethod = "POST", response = Result.class, notes = "新增标签")
    public Result insert(@RequestBody Tags tags){
        return tagsService.insertTag(tags);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiOperation(value = "标签详情", httpMethod = "GET", response = Result.class, notes = "标签详情")
    public Result getTagsById(Long id){
        return tagsService.getTagsById(id);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改标签", httpMethod = "POST", response = Result.class, notes = "修改标签")
    public Result update(@RequestBody Tags tags){
        return tagsService.updateTag(tags);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除标签", httpMethod = "DELETE", response = Result.class, notes = "删除标签")
    public Result deleteById(Long  id){
        return tagsService.deleteById(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除标签", httpMethod = "DELETE", response = Result.class, notes = "批量删除标签")
    public Result deleteBatch(@RequestBody List<Long> ids){
        return tagsService.deleteBatch(ids);
    }

}
