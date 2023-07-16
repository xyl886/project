package com.love.product.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;

@Component
public class WebSocketEventListener {

    @Resource
    private SimpMessageSendingOperations messagingTemplate;

    public Integer userNum = 0;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        userNum++;
        messagingTemplate.convertAndSend("/topic/ServerToClient.showUserNumber", userNum);
        System.out.println("我来了哦~");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        userNum--;
        messagingTemplate.convertAndSend("/topic/ServerToClient.showUserNumber", userNum);
        System.out.println("我走了哦~");
    }
}
