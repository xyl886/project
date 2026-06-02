package com.love.product.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 通知处理器
 * 管理用户连接，推送通知和私信
 */
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationWebSocketHandler.class);
    // userId -> WebSocketSession
    private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /**
     * 连接建立后，用 userId 标识会话
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = extractUserId(session);
        if (userId != null) {
            userSessions.put(userId, session);
            log.info("WebSocket 连接建立: userId={}", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 前端可能发心跳或聊天消息
        String payload = message.getPayload();
        try {
            JSONObject json = JSON.parseObject(payload);
            String type = json.getString("type");
            if ("ping".equals(type)) {
                session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
            } else if ("chat".equals(type)) {
                // 转发私信
                Long toUserId = json.getLong("toUserId");
                WebSocketSession toSession = userSessions.get(toUserId);
                if (toSession != null && toSession.isOpen()) {
                    toSession.sendMessage(new TextMessage(payload));
                }
            }
        } catch (Exception e) {
            log.warn("WebSocket 消息处理失败", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = extractUserId(session);
        if (userId != null) {
            userSessions.remove(userId);
            log.info("WebSocket 连接关闭: userId={}", userId);
        }
    }

    /**
     * 向指定用户推送通知
     */
    public static void sendNotification(Long userId, String title, String content, int type) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                JSONObject msg = new JSONObject();
                msg.put("type", "notification");
                msg.put("title", title);
                msg.put("content", content);
                msg.put("notifyType", type);
                session.sendMessage(new TextMessage(msg.toJSONString()));
            } catch (IOException e) {
                log.error("推送通知失败 userId={}", userId, e);
            }
        }
    }

    /**
     * 向指定用户推送私信
     */
    public static void sendChatMessage(Long toUserId, Long fromUserId, String fromNickname, String message) {
        WebSocketSession session = userSessions.get(toUserId);
        if (session != null && session.isOpen()) {
            try {
                JSONObject msg = new JSONObject();
                msg.put("type", "chat");
                msg.put("fromUserId", fromUserId);
                msg.put("fromNickname", fromNickname);
                msg.put("message", message);
                session.sendMessage(new TextMessage(msg.toJSONString()));
            } catch (IOException e) {
                log.error("推送私信失败 toUserId={}", toUserId, e);
            }
        }
    }

    private Long extractUserId(WebSocketSession session) {
        String query = session.getUri() != null ? session.getUri().getQuery() : "";
        if (query != null && query.contains("userId=")) {
            try {
                return Long.parseLong(query.split("userId=")[1].split("&")[0]);
            } catch (Exception e) {
                log.warn("提取 userId 失败: {}", query);
            }
        }
        // 尝试从 session 属性获取
        Object userId = session.getAttributes().get("userId");
        return userId instanceof Long ? (Long) userId : null;
    }
}
