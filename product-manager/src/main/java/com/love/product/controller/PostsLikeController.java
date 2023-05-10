package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.service.PostsLikeService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 点赞controller
 */
@Api(tags = "点赞")
@Slf4j
@RestController
@RequestMapping("/postsLike")
public class PostsLikeController {

    @Resource
    private PostsLikeService postsLikeService;

    @ApiOperation("新增点赞")
    @GetMapping("/add")
    public Result<?> add(@RequestParam("postsId") Long postsId, @RequestParam("deleted") Integer deleted) {
        return postsLikeService.add(JwtUtil.getUserId(),postsId,deleted);
    }
}