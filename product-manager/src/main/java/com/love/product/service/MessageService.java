package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.MessageDTO;
import com.love.product.entity.req.MessagePageReq;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: MessageService
 * @Author: Administrator
 * @Date: 2023/7/4 11:47
 */

public interface MessageService extends IService<Message> {

    Result listMessage(MessagePageReq messagePageReq);

    Result passBatch(List<Integer> ids);

    Result deleteMessageById(int id);

    Result deleteBatch(List<Integer> ids);

    Result insertMessage(MessageDTO messageDTO);

    Result selectMessageList();
}
