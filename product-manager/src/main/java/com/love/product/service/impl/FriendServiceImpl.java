package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Friend;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.entity.vo.FollowVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enums.Role;
import com.love.product.mapper.FriendMapper;
import com.love.product.service.FriendService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: FriendServiceImpl
 * @Author: Administrator
 * @Date: 2023/8/15 15:45
 */
@Service
@Slf4j
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    @Resource
    private UserInfoService userInfoService;
    @Override
    public Result addFriend(Long friendUserId) {
        UserInfo userInfo = userInfoService.getById(friendUserId);
        if(userInfo != null){
            Friend friend =  Friend.builder()
                    .userId(JwtUtil.getUserId())
                    .friendId(userInfo.id)
                    .status(0)
                    .build();
            friend.setDeleted(0);
     Friend row=  baseMapper.selectOne(new QueryWrapper<Friend>()
             .eq("friend_id",userInfo.id)
                  .eq(("user_id"),JwtUtil.getUserId()).last("limit 1"));
     log.info(String.valueOf(row));
            if(row==null) {
                baseMapper.add(friend);
            }
            return Result.OK();
        }else{
            return Result.failMsg("用户不存在");
        }
    }

    @Override
    public ResultPage getFriendList(Long userId, FriendPageReq friendPageReq) {
        LambdaQueryWrapper<Friend> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Friend::getUserId, userId)
                .or()
                .eq(Friend::getFriendId, userId)
                .orderByAsc(Friend::getCreateTime);
        Page<Friend> page=page(friendPageReq.build(),lambdaQueryWrapper);
        new ArrayList<>();
        if (page.getTotal() > 0) {
            new ArrayList<>(page.getRecords());
            log.info(page.getRecords().toString());
        }
        List<FollowVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream()
                    .filter(friend -> friend.getUserId().equals(userId))
                    .map(friend -> {
                FollowVO followVO = new FollowVO();
                followVO.setUserId(friend.getUserId());
                followVO.setBeFollowedUserId(friend.getFriendId());
                // Set other properties of followVO as needed
                userIds.add(friend.getFriendId());
                return followVO;
            }).collect(Collectors.toList());
        }

        // Fetch additional data for userIds
        Map<Long, UserInfoVO> userInfoVOMap;
        if (!userIds.isEmpty()) {
            userInfoVOMap = userInfoService.listByIds(userIds);
        } else {
            userInfoVOMap = new HashMap<>();
        }

        list.forEach(item -> {
            UserInfoVO userInfoVO = userInfoVOMap.get(item.getBeFollowedUserId());
            UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
            BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
            userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
            item.setUserInfo(userBasicInfoVO);
        });

        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 好友列表单向删除聊天
     * @param friendUserId 好友id
     * @return
     */
    @Override
    public Result deleteFriend(Long friendUserId) {
        Long id=JwtUtil.getUserId();
        LambdaUpdateWrapper<Friend> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Friend::getUserId,id)
                .eq(Friend::getFriendId, friendUserId)
                .set(Friend::getStatus, 1);
        boolean success = update(lambdaUpdateWrapper);
        if (success) {
            return Result.OK();
        } else {
            return Result.failMsg("删除失败！");
        }
    }
}
