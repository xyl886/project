package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.constant.RedisConstant;
import com.love.product.entity.Follow;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FollowPageReq;
import com.love.product.entity.vo.FollowVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.Role;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.FollowMapper;
import com.love.product.service.FollowService;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private FollowMapper followMapper;
    @Resource
    private RedisService redisService;

    @Override
    public Result<?> add(Long userId, Long beFollowedUserId,Integer deleted) {
        UserInfo userInfo = userInfoService.getById(beFollowedUserId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        if(userInfo != null){
            LocalDateTime now = LocalDateTime.now();
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setBeFollowedUserId(userInfo.getId());
            follow.setDeleted(yesOrNo.getValue());
            follow.setCreateTime(now);
            follow.setUpdateTime(now);
            followMapper.add(follow);
            //清除redis缓存
            redisService.del(RedisConstant.FOLLOW_NUM + userId);
            redisService.del(RedisConstant.FANS_NUM + beFollowedUserId);
            if(yesOrNo.equals(YesOrNo.YES)){
                return Result.OKMsg("已取消关注");
            }else{
                return Result.OKMsg("关注成功");
            }
        }else{
            return Result.failMsg("关注的对象不存在");
        }
    }

    @Override
    public Follow getDetail(Long userId, Long beFollowedUserId) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,userId)
                .eq(Follow::getBeFollowedUserId,beFollowedUserId)
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public int getFansNumByUserId(Long userId){
        Integer num = (Integer) redisService.get(RedisConstant.FANS_NUM + userId);
        if(num == null){
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getBeFollowedUserId,userId);
            long fansNum = count(queryWrapper);
            num = Integer.parseInt(String.valueOf(fansNum));
            redisService.set(RedisConstant.FANS_NUM + userId, num, 7L, TimeUnit.DAYS);
        }
        return num;
    }

    @Override
    public int getFollowNumByUserId(Long userId){
        Integer num = (Integer) redisService.get(RedisConstant.FOLLOW_NUM + userId);
        if(num == null){
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getUserId,userId);
            long followNum = count(queryWrapper);
            num = Integer.parseInt(String.valueOf(followNum));
            redisService.set(RedisConstant.FOLLOW_NUM + userId, num, 7L, TimeUnit.DAYS);
        }
        return num;
    }

    /**
     * 我关注的人中关注我的
     */
    public Map<Long, Follow> listFollowInUserId(List<Long> userIds, Long beFollowedUserId) {
        Map<Long, Follow> followMap = new HashMap<>();
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Follow::getUserId,userIds)
                .eq(Follow::getBeFollowedUserId,beFollowedUserId);
        List<Follow> follows = list(queryWrapper);
        follows.forEach(item-> followMap.put(item.getUserId(), item));
        return followMap;
    }

    /**
     * 粉丝中我关注的粉丝
     */
    public Map<Long, Follow> listFollowInFansId(Long userId, List<Long> beFollowedUserIds) {
        Map<Long, Follow> followMap = new HashMap<>();
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,userId)
                .in(Follow::getBeFollowedUserId,beFollowedUserIds);
        List<Follow> follows = list(queryWrapper);
        follows.forEach(item-> followMap.put(item.getBeFollowedUserId(), item));
        return followMap;
    }

    /**
     * 分页
     */
    @Override
    public ResultPage<FollowVO> getPage(Long userId, FollowPageReq followPageReq) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.equals(followPageReq.getFollowType(),1), Follow::getUserId, userId);
        queryWrapper.eq(Objects.equals(followPageReq.getFollowType(),2), Follow::getBeFollowedUserId, userId);
        queryWrapper.orderByDesc(Follow::getCreateTime);
        Page<Follow> page = page(followPageReq.build(), queryWrapper);
        List<FollowVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(follow -> {
                FollowVO followVO = BeanUtil.copyProperties(follow, FollowVO.class);
                if(Objects.equals(followPageReq.getFollowType(),1)){
                    userIds.add(followVO.getBeFollowedUserId());
                }else{
                    userIds.add(followVO.getUserId());
                }
                return followVO;
            }).collect(Collectors.toList());
        }
        Map<Long, UserInfoVO> userInfoVOMap;
        Map<Long, Follow> followMap = null;
        if(userIds.size() > 0){
            if(Objects.equals(followPageReq.getFollowType(),1)){
                followMap = listFollowInUserId(userIds,userId);
            }else{
                followMap = listFollowInFansId(userId,userIds);
            }
        }
        if(userIds.size() > 0){
            userInfoVOMap = userInfoService.listByIds(userIds);
            Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
            Map<Long, Follow> finalFollowMap = followMap;
            list.forEach(item -> {
                UserInfoVO userInfoVO;
                int followStatus;
                Follow follow;
                if(Objects.equals(followPageReq.getFollowType(),1)){
                    follow = finalFollowMap.get(item.getBeFollowedUserId());
                    followStatus = 1;//我关注了Ta
                    userInfoVO = finalUserInfoVOMap.get(item.getBeFollowedUserId());
                }else{
                    follow = finalFollowMap.get(item.getUserId());
                    followStatus = 2;//Ta关注了我
                    userInfoVO = finalUserInfoVOMap.get(item.getUserId());
                }
                if(follow != null){
                    followStatus = 3;//互关
                }
                item.setFollowStatus(followStatus);
                item.setUserInfo(new UserBasicInfoVO(
                        userInfoVO.id,userInfoVO.nickname,userInfoVO.avatar, Role.valueOf(userInfoVO.role).getText()));
            });
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /*
    我的关注
    */
    @Override
    public List<Long> getFollowedUserIdsByUserId(Long userId) {
        List<Long> followedUserIds = new ArrayList<>();
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,userId).eq(Follow::getDeleted, YesOrNo.NO.getValue());
        List<Follow> follows = list(queryWrapper);
        log.info("follows:" + follows);
        follows.forEach(item-> followedUserIds.add(item.getBeFollowedUserId()));
        return followedUserIds;
    }
}
