package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.service.PostsCommentService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 评论controller
 */
@Api(tags = "评论")
@Slf4j
@RestController
@RequestMapping("/postsComment")
public class PostsCommentController {

    @Resource
    private PostsCommentService postsCommentService;

    @ApiOperation("新增评论")
    @PostMapping("/add")
    public Result<?> add(@RequestParam("postsId") Long postsId, @RequestParam("content") String content) {
        return postsCommentService.add(JwtUtil.getUserId(),postsId,content);
    }

    @ApiOperation("评论列表")
    @GetMapping("/listByPostsId")
    public Result<?> listByPostsId(@RequestParam("postsId") Long postsId) {
        return postsCommentService.listByPostsId(postsId);
    }
    @ApiOperation("新增评论点赞")
    @GetMapping("/addCommentLike")
    public Result<?> add(@RequestParam("commentId") Long commentId, @RequestParam("deleted") Integer deleted) {
        return postsCommentService.addCommentLike(JwtUtil.getUserId(),commentId,deleted);
    }
    @ApiOperation("删除评论")
    @GetMapping("/del")
    public Result<?> del(@RequestParam("id") Long id) {
        return postsCommentService.del(JwtUtil.getUserId(),id);
    }
}
