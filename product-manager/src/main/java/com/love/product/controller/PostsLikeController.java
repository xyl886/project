package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.service.PostsLikeService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Administrator
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
        return postsLikeService.add(JwtUtil.getUserId(), postsId, deleted);
    }

    @ApiOperation("点赞")
    @GetMapping("like")
    public Result<?> like(@RequestParam("postsId") Long postsId) {
        return postsLikeService.like(postsId, JwtUtil.getUserId());
    }

    @ApiOperation("取消点赞")
    @GetMapping("dislike")
    public Result<?> dislike(@RequestParam("postsId") Long postsId) {
        return postsLikeService.dislike(postsId, JwtUtil.getUserId());
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<HistoryVO> getPage(@RequestBody HistoryPageReq pageQuery) {
        return postsLikeService.getPage(JwtUtil.getUserId(), pageQuery);
    }
}
