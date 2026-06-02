package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.love.product.entity.Notification;
import com.love.product.mapper.NotificationMapper;
import com.love.product.service.NotificationService;
import com.love.product.websocket.NotificationWebSocketHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    public void notify(Long userId, Long fromUserId, int type, String content, Long targetId, Integer targetType) {
        Notification notif = new Notification();
        notif.setId(IdWorker.getId());
        notif.setUserId(userId);
        notif.setFromUserId(fromUserId);
        notif.setType(type);
        notif.setContent(content);
        notif.setTargetId(targetId);
        notif.setTargetType(targetType);
        notif.setIsRead(0);
        notif.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notif);

        // WebSocket 实时推送
        String title;
        switch (type) {
            case 1: title = "点赞了你的帖子"; break;
            case 2: title = "评论了你的帖子"; break;
            case 3: title = "回复了你的评论"; break;
            case 4: title = "关注了你"; break;
            default: title = "系统通知";
        }
        NotificationWebSocketHandler.sendNotification(userId, title, content, type);
    }
}
