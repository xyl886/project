package com.love.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.love.product.entity.Follow;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.service.MessageService;
import com.love.product.service.WebSocketService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product.controller
 * @Description: MessageController
 * @Author: Administrator
 * @Date: 2023/7/4 11:09
 */
@RestController
@Slf4j
@Api("消息")
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * @Description: 添加一条消息
     * @Date: 2023/6/24 18:37
     * @Author: timeless
     */
    @PostMapping("/saveOneMsg")
    public void saveOneMsg(@RequestBody Message Message) {
        messageService.save(Message);
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
        return messageService.listMessages(fromId,toId);
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
        return messageService.remove(Id,fromId,toId);
    }














}
