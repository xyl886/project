package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;

/**
 * @PackageName: com.love.product.service
 * @Description: MessageService
 * @Author: Administrator
 * @Date: 2023/7/4 11:47
 */

public interface MessageService extends IService<Message> {
    Result<?> listMessages(Long formId,Long toId);
    Result<?> remove(Long Id,Long fromId, Long toId);

    void sendChatMessage(Message message);
}
