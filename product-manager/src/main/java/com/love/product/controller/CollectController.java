package com.love.product.controller;

import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.vo.CollectVO;
import com.love.product.service.CollectService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 收藏controller
 */
@Api(tags = "收藏")
@Slf4j
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @ApiOperation("新增收藏")
    @GetMapping("/add")
    public Result<?> add(@RequestParam("postsId") Long postsId,@RequestParam("deleted") Integer deleted) {
        return collectService.add(JwtUtil.getUserId(),postsId,deleted);
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<CollectVO> getPage(@RequestBody PageQuery pageQuery) {
        return collectService.getPage(JwtUtil.getUserId(),pageQuery);
    }
}