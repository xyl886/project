package com.love.product.controller;

import com.love.product.entity.ChatMessage;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.ChatMessageDTO;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.service.ChatMessageService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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


    @PostMapping("/addFriend")
    public Result<?> addFriend(@RequestParam("id") Long friendUserId) {
        chatmessageService.addFriend(friendUserId);
        return Result.OKMsg("操作成功");
    }

    @PostMapping("/getFriendList")
    public ResultPage<?> getFriendList(@RequestBody FriendPageReq friendPageReq) {
        return chatmessageService.getFriendList(JwtUtil.getUserId(), friendPageReq);
    }

    @DeleteMapping("/deleteFriend")
    public Result<?> deleteFriend(@RequestParam("friendUserId") Long friendUserId) {
        chatmessageService.deleteFriend(friendUserId);
        return Result.OKMsg("删除好友成功");
    }


    @PostMapping("/saveOneMsg")
    public Result<?> saveOneMsg(@RequestBody ChatMessage msg) {
        // 检查私信限制：未互关前只能发1条
        Long fromId = msg.getFromId();
        Long toId = msg.getToId();
        if (!chatmessageService.canSendMessage(fromId, toId)) {
            return Result.failMsg("对方未回复前只能发送一条私信，请等待回复或互相关注后畅聊");
        }
        chatmessageService.save(msg);
        return Result.OK();
    }

    /**
     * 获取与某用户的私聊记录
     *   因为我们要放寒假
     * @param fromId 当前用户id
     * @param toId   查询私聊信息用户id
     * @return Result.OK(messages)
     */
    @GetMapping("/listMessages")
    public Result<List<ChatMessageDTO>> listMessages(
            @RequestParam("fromId") Long fromId,
            @RequestParam("toId") Long toId) {
        return Result.OK(chatmessageService.listMessages(fromId, toId));
    }

    /**
     * @Description: 删除所有聊天记录
     * @Date: 2023/6/24 20:27
     * @Author: timeless
     */
    @DeleteMapping("/deleteMsg")
    public Result<?> deleteMsg(
            @RequestParam("id") Long Id,
            @RequestParam("fromId") Long fromId,
            @RequestParam("toId") Long toId) {
        chatmessageService.remove(Id, fromId, toId);
        return Result.OK();
    }

}
