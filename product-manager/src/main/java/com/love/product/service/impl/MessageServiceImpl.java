package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.Exception.BizException;
import com.love.product.entity.Follow;
import com.love.product.entity.Message;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.MessageDTO;
import com.love.product.mapper.MessageMapper;
import com.love.product.service.FileUploadService;
import com.love.product.service.MessageService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: MessageServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/4 11:48
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Resource
    private SimpMessagingTemplate template;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private MessageService messageService;

    /**
     * 简单点对点聊天室
     */
    public void sendChatMessage(Message message) {
        //可以看出template最大的灵活就是我们可以获取前端传来的参数来指定订阅地址
        //前面参数是订阅地址，后面参数是消息信息
        template.convertAndSend("/topic/ServerToClient.private." + message.getToId(), message);
        // 消息存储到数据库
        boolean save = messageService.saveOrUpdate(message);
        if(!save){
            throw new BizException();
        }
    }
    @Override
    public Result<List<MessageDTO>> listMessages(Long fromId, Long toId) {
     List<MessageDTO> messages=messageMapper.listMessages(fromId,toId);
     messages.forEach(message ->{
         message.setSentByMe(message.getFromId().equals(fromId));
         message.setFromIdAvatar(fileUploadService.getImgPath(message.getFromIdAvatar()));
     });
        log.info(messages.toString());
        return Result.OK(messages);
    }

    @Override
    public Result<?> remove(Long Id, Long fromId, Long toId) {
        remove((Wrappers
                .<Message>lambdaQuery()
                .and(q -> q
                        .eq(Message::getFromId, fromId)
                        .eq(Message::getToId, toId)
                        .eq(Message::getId,Id)
                )
                .or(q -> q
                        .eq(Message::getFromId, toId)
                        .eq(Message::getToId, fromId)
                        .eq(Message::getId,Id)
                )));
        Follow follow = Follow.builder().userId(fromId).beFollowedUserId(toId).build();
        template.convertAndSend("/topic/ServerToClient.deleteMsg", follow);
        return Result.OK();
    }
}
