package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FriendPageReq;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: ChatMessageService
 * @Author: Administrator
 * @Date: 2023/8/7 11:18
 */

public interface ChatMessageService extends IService<ChatMessage> {
    Result<?> listMessages(Long formId, Long toId);
    Result<?> remove(Long Id,Long fromId, Long toId);

    void sendChatMessage(ChatMessage chatMessage);

    Result addFriend(Long friendUserId);

    ResultPage getFriendList(Long id, FriendPageReq friendPageReq);

    Result deleteFriend(Long friendUserId);
}
