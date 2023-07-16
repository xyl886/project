package com.love.product.controller;

import com.love.product.entity.Message;
import com.love.product.service.MessageService;
import com.love.product.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.controller
 * @Description: WebSocketController
 * @Author: Administrator
 * @Date: 2023/7/11 10:15
 */
@Slf4j
@RestController
public class WebSocketController {
    @Resource
    private MessageService messageService;

    @MessageMapping("/privateChat")
    public void singleChat(Message message) {
        log.info("在这存消息到数据库："+message);
        messageService.sendChatMessage(message);
    }
}
