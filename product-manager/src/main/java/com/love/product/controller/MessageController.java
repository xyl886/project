package com.love.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.love.product.entity.Follow;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.service.MessageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
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
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private SimpMessagingTemplate template;

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
     * @Description: 查询私聊记录
     * @Date: 2023/6/24 18:40
     * @Author: timeless
     */
    @GetMapping("/listMessages")
    public Result listMessages(
            @RequestParam("fromId") String fromId,
            @RequestParam("toId") String toId) {

        List<String> messages = messageService.list(Wrappers
                        .<Message>lambdaQuery()
                        .and(q -> q
                                .eq(Message::getFromId, fromId)
                                .eq(Message::getToId, toId)
                        )
                        .or(q -> q
                                .eq(Message::getFromId, toId)
                                .eq(Message::getToId, fromId)
                        ))
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());

        return Result.OK(messages);
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
        messageService.remove((Wrappers
                .<Message>lambdaQuery()
                .and(q -> q
                        .eq(Message::getFromId, fromId)
                        .eq(Message::getToId, toId)
                        .eq(Message::getId,Id)
                )
                .or(q -> q
                        .eq(Message::getFromId, toId)
                        .eq(Message::getToId, fromId)
                        .eq(Message::getId,Id)
                )));
        Follow follow = Follow.builder().userId(fromId).beFollowedUserId(toId).build();
        template.convertAndSend("/topic/ServerToClient.deleteMsg", follow);
        return Result.OK();
    }














}
