package com.love.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Message;
import com.love.product.mapper.MessageMapper;
import com.love.product.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: MessageServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/4 11:48
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
