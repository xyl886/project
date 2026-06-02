package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FollowPageReq;
import com.love.product.entity.vo.FollowVO;
import com.love.product.service.FollowService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 关注controller
 */
@Api(tags = "关注")
@Slf4j
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @ApiOperation("新增关注")
    @PostMapping("/add")
    public Result<?> add(
            @RequestParam("beFollowedUserId") Long beFollowedUserId,
            @RequestParam("deleted") Integer deleted) {
        followService.add(JwtUtil.getUserId(), beFollowedUserId, deleted);
        return Result.OKMsg(deleted != null && deleted.equals(1) ? "已取消关注" : "关注成功");
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<FollowVO> getPage(@RequestBody FollowPageReq followPageReq) {
        return followService.getPage(JwtUtil.getUserId(),followPageReq);
    }
}
