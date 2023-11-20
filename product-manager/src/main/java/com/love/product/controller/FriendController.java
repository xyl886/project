package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.service.FriendService;
import com.love.product.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.controller
 * @Description: FriendController
 * @Author: Administrator
 * @Date: 2023/8/15 15:45
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    @Resource
    private FriendService friendService;
    @PostMapping("/addFriend")
    public Result addFriend(@RequestParam("id") Long friendUserId){
        return  friendService.addFriend(friendUserId);

    }
    @PostMapping("/getFriendList")
    public ResultPage getFriendList(@RequestBody FriendPageReq friendPageReq){
        return  friendService.getFriendList(JwtUtil.getUserId(),friendPageReq);

    }
    @DeleteMapping("/deleteFriend")
    public Result deleteFriend(@RequestParam("friendUserId") Long friendUserId){

        return  friendService.deleteFriend(friendUserId);

    }
}
