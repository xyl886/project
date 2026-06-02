package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.Friend;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.ChatMessageDTO;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.enums.YesOrNo;
import com.love.product.mapper.ChatMessageMapper;
import com.love.product.mapper.FriendMapper;
import com.love.product.service.ChatMessageService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: ChatMessageServiceImpl
 * @Author: Administrator
 * @Date: 2023/8/7 11:19
 */
@Service
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private ChatMessageService chatMessageService;
    /**
     * 保存聊天消息
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        boolean save = chatMessageService.saveOrUpdate(chatMessage);
        if(!save){
            throw new BizException();
        }
    }

    @Override
    public Result addFriend(Long friendUserId) {
        UserInfo userInfo = userInfoService.getById(friendUserId);
        YesOrNo yesOrNo = YesOrNo.fromValue(0);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        if(userInfo != null){
            LocalDateTime now = LocalDateTime.now();
            Friend follow = new Friend();
            follow.setUserId(JwtUtil.getUserId());
            follow.setFriendId(userInfo.id);
            follow.setDeleted(yesOrNo.getValue());
            follow.setCreateTime(now);
            follow.setUpdateTime(now);
            friendMapper.add(follow);
            return Result.OKMsg("操作成功");
        }else{
            return Result.failMsg("用户不存在");
        }
    }

    @Override
    public ResultPage getFriendList(Long id, FriendPageReq friendPageReq) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.equals(friendPageReq.getFollowType(), 1), Friend::getUserId, id);
        queryWrapper.eq(Objects.equals(friendPageReq.getFollowType(), 2), Friend::getFriendId, id);
        Page<Friend> page = friendMapper.selectPage(friendPageReq.build(), queryWrapper);
        List<Friend> list = page.getRecords();
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public Result deleteFriend(Long friendUserId) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, JwtUtil.getUserId())
                .eq(Friend::getFriendId, friendUserId);
        friendMapper.delete(queryWrapper);
        return Result.OKMsg("删除好友成功");
    }

    @Override
    public Result<List<ChatMessageDTO>> listMessages(Long fromId, Long toId) {
        List<ChatMessageDTO> messages=chatMessageMapper.listMessages(fromId,toId);
        messages.forEach(message -> message.setSentByMe(message.getFromId().equals(fromId)));
        log.info(messages.toString());
        return Result.OK(messages);
    }

    @Override
    public Result<?> remove(Long Id, Long fromId, Long toId) {
        remove((Wrappers
                .<ChatMessage>lambdaQuery()
                .and(q -> q
                        .eq(ChatMessage::getFromId, fromId)
                        .eq(ChatMessage::getToId, toId)
                        .eq(ChatMessage::getId,Id)
                )
                .or(q -> q
                        .eq(ChatMessage::getFromId, toId)
                        .eq(ChatMessage::getToId, fromId)
                        .eq(ChatMessage::getId,Id)
                )));
        return Result.OK();
    }
}
