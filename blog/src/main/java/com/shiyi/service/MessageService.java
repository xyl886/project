package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
public interface MessageService extends IService<Message> {

    Result listMessage(String name);

    Result deleteMessageById(int id);

    Result passBatch(List<Integer> ids);

    Result deleteBatch(List<Integer> ids);




    //    ------web端方法开始-----
    Result webAddMessage(Message message);

    Result webMessage();


}
