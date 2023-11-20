package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.Exception.BizException;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.Follow;
import com.love.product.entity.Friend;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.ChatMessageDTO;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.entity.vo.FollowVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.Role;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.ChatMessageMapper;
import com.love.product.mapper.FriendMapper;
import com.love.product.service.ChatMessageService;
import com.love.product.service.FileUploadService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private SimpMessagingTemplate template;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private ChatMessageService chatMessageService;
    /**
     * 简单点对点聊天室
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        //可以看出template最大的灵活就是我们可以获取前端传来的参数来指定订阅地址
        //前面参数是订阅地址，后面参数是消息信息
        template.convertAndSend("/topic/ServerToClient.private." + chatMessage.getToId(), chatMessage);
        // 消息存储到数据库
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
        queryWrapper.eq(Objects.equals(friendPageReq.getFollowType(),1), Friend::getUserId, id);
        queryWrapper.eq(Objects.equals(friendPageReq.getFollowType(),2), Friend::getFriendId, id);
        queryWrapper.orderByDesc(Friend::getCreateTime);
//        Page<Friend> page = friendMapper.selectFriendList(
//                friendPageReq.getCurrentPage(),friendPageReq.getPageSize(),friendPageReq);
//        List<FollowVO> list = new ArrayList<>();
//        List<Long> userIds = new ArrayList<>();
//        if (page.getTotal() > 0) {
//            list = page.getRecords().stream().map(follow -> {
//                FollowVO followVO = BeanUtil.copyProperties(follow, FollowVO.class);
//                if(Objects.equals(friendPageReq.getFollowType(),1)){
//                    userIds.add(followVO.getBeFollowedUserId());
//                }else{
//                    userIds.add(followVO.getUserId());
//                }
//                return followVO;
//            }).collect(Collectors.toList());
//        }
//        Map<Long, UserInfoVO> userInfoVOMap;
//        Map<Long, Follow> followMap = null;
//        if(userIds.size() > 0){
//            if(Objects.equals(friendPageReq.getFollowType(),1)){
//                followMap = listFollowInUserId(userIds,userId);
//            }else{
//                followMap = listFollowInFansId(userId,userIds);
//            }
//        }
//        if(userIds.size() > 0){
//            userInfoVOMap = userInfoService.listByIds(userIds);
//            Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
//            Map<Long, Follow> finalFollowMap = followMap;
//            list.forEach(item -> {
//                UserInfoVO userInfoVO;
//                int followStatus;
//                Follow follow;
//                if(Objects.equals(friendPageReq.getFollowType(),1)){
//                    follow = finalFollowMap.get(item.getBeFollowedUserId());
//                    followStatus = 1;//我关注了Ta
//                    userInfoVO = finalUserInfoVOMap.get(item.getBeFollowedUserId());
//                }else{
//                    follow = finalFollowMap.get(item.getUserId());
//                    followStatus = 2;//Ta关注了我
//                    userInfoVO = finalUserInfoVOMap.get(item.getUserId());
//                }
//                if(follow != null){
//                    followStatus = 3;//互关
//                }
//                item.setFollowStatus(followStatus);
//
//                UserBasicInfoVO userBasicInfoVO=new UserBasicInfoVO();
//                BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
//                userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
//                item.setUserInfo(userBasicInfoVO);
//            });
//        }
//        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
        return null;
    }

    @Override
    public Result deleteFriend(Long friendUserId) {

        return null;
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
        Follow follow = Follow.builder().userId(fromId).beFollowedUserId(toId).build();
        template.convertAndSend("/topic/ServerToClient.deleteMsg", follow);
        return Result.OK();
    }
}
