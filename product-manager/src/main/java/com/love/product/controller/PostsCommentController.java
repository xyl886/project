package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.vo.PostsCommentVO;
import com.love.product.service.PostsCommentService;
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
    public Result<?> add(@RequestParam("postsId") Long postsId,
                         @RequestParam("content") String content,
                         @RequestParam(value = "parentCommentId", required = false) Long parentCommentId) {
        postsCommentService.add(JwtUtil.getUserId(), postsId, content, parentCommentId);
        return Result.OK("评论成功");
    }

    @ApiOperation("评论列表")
    @GetMapping("/listByPostsId")
    public Result<List<PostsCommentVO>> listByPostsId(@RequestParam("postsId") Long postsId) {
        return Result.OK(postsCommentService.listByPostsId(postsId));
    }
    @ApiOperation("新增评论点赞")
    @PostMapping("/addCommentLike")
    public Result<?> add(@RequestParam("commentId") Long commentId, @RequestParam("deleted") Integer deleted) {
        postsCommentService.addCommentLike(JwtUtil.getUserId(), commentId, deleted);
        return Result.OKMsg(deleted != null && deleted.equals(1) ? "已取消点赞" : "点赞成功");
    }
    @ApiOperation("删除评论")
    @DeleteMapping("/del")
    public Result<?> del(@RequestParam("id") Long id) {
        postsCommentService.del(JwtUtil.getUserId(), id);
        return Result.OK("删除成功");
    }
}
