package com.love.product.controller;

import com.love.product.entity.ChatMessage;
import com.love.product.service.ChatMessageService;
import com.love.product.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    private ChatMessageService chatMessageService;

    @MessageMapping("/privateChat")
    public void singleChat(ChatMessage chatMessage) {
        log.info("在这存消息到数据库："+ chatMessage);
        chatMessageService.sendChatMessage(chatMessage);
    }
}
