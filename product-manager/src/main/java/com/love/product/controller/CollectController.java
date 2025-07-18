package com.love.product.controller;

import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CollectPageReq;
import com.love.product.entity.vo.CollectVO;
import com.love.product.service.CollectService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
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
    public Result<?> add(@RequestParam("postsIds")Long[] postsIds,@RequestParam("deleted") Integer deleted) {
        return collectService.add(JwtUtil.getUserId(), deleted, postsIds);
    }
    @ApiOperation("批量取消收藏")
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Long> postsIds) {
        return collectService.deleteBatch(JwtUtil.getUserId(),postsIds);
    }
    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<CollectVO> getPage(@RequestBody CollectPageReq pageQuery) {
        return collectService.getPage(JwtUtil.getUserId(),pageQuery);
    }
}
