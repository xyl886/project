package com.love.product.service;

public interface NotificationService {
    /**
     * 发送通知
     * @param userId 接收用户ID
     * @param fromUserId 触发用户ID
     * @param type 类型 1点赞 2评论 3回复 4关注 5系统
     * @param content 内容摘要
     * @param targetId 目标ID
     * @param targetType 目标类型 1帖子 2评论
     */
    void notify(Long userId, Long fromUserId, int type, String content, Long targetId, Integer targetType);
}
