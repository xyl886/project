package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Message;
import com.love.product.entity.base.ResultPage;
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

    ResultPage listMessage(MessagePageReq messagePageReq);

    void passBatch(List<Integer> ids);

    void deleteMessageById(int id);

    void deleteBatch(List<Integer> ids);

    void insertMessage(MessageDTO messageDTO);

    List<MessageDTO> selectMessageList();
}
