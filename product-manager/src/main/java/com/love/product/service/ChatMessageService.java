package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.ChatMessageDTO;
import com.love.product.entity.req.FriendPageReq;

import java.util.List;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: ChatMessageService
 * @Author: Administrator
 * @Date: 2023/8/7 11:18
 */

public interface ChatMessageService extends IService<ChatMessage> {
    List<ChatMessageDTO> listMessages(Long formId, Long toId);
    boolean canSendMessage(Long fromId, Long toId);
    void remove(Long Id, Long fromId, Long toId);

    void sendChatMessage(ChatMessage chatMessage);

    void addFriend(Long friendUserId);

    ResultPage getFriendList(Long id, FriendPageReq friendPageReq);

    void deleteFriend(Long friendUserId);
}
