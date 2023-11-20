package com.love.product.controller;

import com.love.product.entity.ChatMessage;
import com.love.product.entity.base.Result;
import com.love.product.entity.req.FollowPageReq;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.service.ChatMessageService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.controller
 * @Description: ChatMessageController
 * @Author: Administrator
 * @Date: 2023/7/4 11:09
 */
@RestController
@Slf4j
@Api("消息")
@RequestMapping("/chat_message")
public class ChatMessageController {

    @Resource
    private ChatMessageService chatmessageService;

    /**
     * @Description: 添加一条消息
     * @Date: 2023/6/24 18:37
     * @Author: timeless
     */

    @PostMapping("/addFriend")
    public Result addFriend(@RequestParam("id") Long friendUserId){
        return  chatmessageService.addFriend(friendUserId);

    }
    @GetMapping("/getFriendList")
    public Result getFriendList(@RequestBody FriendPageReq friendPageReq){
        return chatmessageService.getFriendList(JwtUtil.getUserId(),friendPageReq);
    }
    @DeleteMapping("/deleteFriend")
    public void deleteFriend(@RequestParam("friendUserId") Long friendUserId){
        chatmessageService.deleteFriend(friendUserId);
    }


    @PostMapping("/saveOneMsg")
    public void saveOneMsg(@RequestBody ChatMessage ChatMessage) {
        chatmessageService.save(ChatMessage);
    }

    /**
     * 获取与某用户的私聊记录
     * @param fromId 当前用户id
     * @param toId 查询私聊信息用户id
     * @return Result.OK(messages)
     */
    @GetMapping("/listMessages")
    public Result listMessages(
            @RequestParam("fromId") Long fromId,
            @RequestParam("toId") Long toId) {
        return chatmessageService.listMessages(fromId,toId);
    }

    /**
     * @Description: 删除所有聊天记录
     * @Date: 2023/6/24 20:27
     * @Author: timeless
     */
    @DeleteMapping("/deleteMsg")
    public Result deleteMsg(
            @RequestParam("id") Long Id,
            @RequestParam("fromId") Long fromId,
            @RequestParam("toId") Long toId) {
        return chatmessageService.remove(Id,fromId,toId);
    }














}
