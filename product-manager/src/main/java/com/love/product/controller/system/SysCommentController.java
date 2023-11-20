package com.love.product.controller.system;

import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CommentPageReq;
import com.love.product.service.PostsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysCommentController
 * @Author: Administrator
 * @Date: 2023/8/8 20:18
 */
@RestController
@RequestMapping("/system/comment")
@Api(tags = "评论管理")
public class SysCommentController {
    @Resource
    private PostsCommentService postsCommentService;
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "评论列表", httpMethod = "GET", response = Result.class, notes = "评论列表")
    public ResultPage list(@RequestBody CommentPageReq commentPageReq){
        return postsCommentService.listComment(commentPageReq);
    }
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除评论", httpMethod = "POST", response = Result.class, notes = "修改文章")
    public Result delete(@RequestParam("id") Long id) {
        return postsCommentService.deleteComment(id);
    }
    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除评论", httpMethod = "DELETE", response = Result.class, notes = "批量删除评论")
    public Result deleteBatch(@RequestBody List<Long> ids){
        return postsCommentService.deleteBatch(ids);
    }
}
