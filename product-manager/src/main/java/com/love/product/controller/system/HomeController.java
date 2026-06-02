package com.love.product.controller.system;

import com.alibaba.fastjson2.JSONArray;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.HomeVO;
import com.love.product.mapper.TagsMapper;
import com.love.product.service.HomeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: HomeController
 * @Author: Administrator
 * @Date: 2023/7/21 14:49
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HomeService homeService;
    @Resource
    private TagsMapper tagsMapper;
    @GetMapping(value = "/listAllTags")
    @ApiOperation(value = "标签词云")
    public Result<?> tagsCount(){
        return Result.OK(tagsMapper.selectAll());
    }
    @GetMapping(value = "/lineCount")
    @ApiOperation(value = "首页统计", httpMethod = "GET", response = Result.class, notes = "首页统计")
    public Result<Map<String, Integer>> lineCount() {
        return Result.OK(homeService.lineCount());
    }
    @GetMapping(value = "/init")
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = Result.class, notes = "首页各种统计信息")
    public Result<HomeVO> init() {
        return Result.OK(homeService.init());
    }
    @GetMapping("/hot")
    @ApiOperation(value = "获取各大平台热搜", httpMethod = "GET", response = Result.class, notes = "获取各大平台热搜")
    public Result<JSONArray> hot(String type){
        return Result.OK(homeService.hot(type));
    }
}
