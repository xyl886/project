package com.love.product.controller;

import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.enums.YesOrNo;
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
    @PostMapping("/add")
    public Result<?> add(@RequestParam("postsId") Long postsId, @RequestParam("deleted") Integer deleted) {
        return postsLikeService.add(JwtUtil.getUserId(), postsId, deleted);
    }

    @ApiOperation("查询点赞状态")
    @GetMapping("/status")
    public Result<?> status(@RequestParam("postsId") Long postsId) {
        Long userId = JwtUtil.getUserId();
        if (userId == null) return Result.OK(false);
        PostsLike like = postsLikeService.getDetail(userId, postsId);
        return Result.OK(like != null && !like.getDeleted().equals(YesOrNo.YES.getValue()));
    }

    @ApiOperation("点赞")
    @PostMapping("like")
    public Result<?> like(@RequestParam("postsId") Long postsId) {
        return postsLikeService.like(postsId, JwtUtil.getUserId());
    }

    @ApiOperation("取消点赞")
    @PostMapping("dislike")
    public Result<?> dislike(@RequestParam("postsId") Long postsId) {
        return postsLikeService.dislike(postsId, JwtUtil.getUserId());
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<HistoryVO> getPage(@RequestBody HistoryPageReq pageQuery) {
        return postsLikeService.getPage(JwtUtil.getUserId(), pageQuery);
    }
}
