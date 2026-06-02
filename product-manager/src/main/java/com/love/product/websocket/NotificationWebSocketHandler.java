package com.love.product.websocket;

import cn.dev33.satoken.stp.StpUtil;
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
    private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

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
        String payload = message.getPayload();
        try {
            JSONObject json = JSON.parseObject(payload);
            String type = json.getString("type");
            if ("ping".equals(type)) {
                session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
            } else if ("chat".equals(type)) {
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
        // 优先用 token 换取 userId
        if (query != null && query.contains("token=")) {
            try {
                String token = query.split("token=")[1].split("&")[0];
                Object loginId = StpUtil.getLoginIdByToken(token);
                if (loginId != null) return Long.valueOf(loginId.toString());
            } catch (Exception e) {
                log.warn("通过 token 获取 userId 失败", e);
            }
        }
        // 兼容旧版：直接传 userId
        if (query != null && query.contains("userId=")) {
            try {
                return Long.parseLong(query.split("userId=")[1].split("&")[0]);
            } catch (Exception e) {
                log.warn("提取 userId 失败: {}", query);
            }
        }
        Object userId = session.getAttributes().get("userId");
        return userId instanceof Long ? (Long) userId : null;
    }
}
