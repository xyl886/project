package com.love.product.util;

import cn.hutool.extra.spring.SpringUtil;
import com.love.product.constant.RedisConstantKey;
import com.love.product.entity.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author hjf
 * @date 2022-11-01 10:34
 * @describe redis工具类
 */
@Slf4j
public class RedisUtil {

    /**
     * 获取redis实例
     *
     * @return RedisTemplate
     */
    private static RedisTemplate getRedisTemplate(){
        return SpringUtil.getBean("redisTemplate", RedisTemplate.class);
    }

    /**
     * 缓存用户
     *
     * @param userInfoVO 用户
     */
    public static void setUser(UserInfoVO userInfoVO){
        getRedisTemplate().opsForValue().set("user:userinfo:" + userInfoVO.getId(),userInfoVO);
    }

    /**
     * 获取用户
     *
     * @param userId 用户ID
     */
    public static UserInfoVO getUser(Long userId){
        return (UserInfoVO) getRedisTemplate().opsForValue().get("user:userinfo:" + userId);
    }

    /**
     * 缓存刷新token
     *
     * @param userId 用户ID
     */
    public static void setRefreshToken(Long userId, String refreshToken){
        getRedisTemplate().opsForValue().set("refresh_token:" + userId,refreshToken);
    }

    /**
     * 获取刷新token
     *
     * @param userId 用户ID
     */
    public static String getRefreshToken(Long userId){
        return (String) getRedisTemplate().opsForValue().get("refresh_token:" + userId);
    }

    /**
     * 用户退出登录 清除相关缓存
     *
     * @param userId 用户ID
     */
    public static void logout(Long userId){
        if(userId != null){
            getRedisTemplate().delete("refresh_token:" + userId);
            getRedisTemplate().delete("user:userinfo:" + userId);
        }
    }

    /**
     * 粉丝数量 保存
     */
    public static void setFansNum(Long userId,int num) {
        getRedisTemplate().opsForValue().set(RedisConstantKey.FANS_NUM + userId, num, 7, TimeUnit.DAYS);
    }

    /**
     * 获取粉丝数量
     */
    public static Integer getFansNum(Long userId) {
        return (Integer) getRedisTemplate().opsForValue().get(RedisConstantKey.FANS_NUM + userId);
    }

    /**
     * 删除粉丝数量
     */
    public static void deleteFansNum(Long userId) {
        getRedisTemplate().delete(RedisConstantKey.FANS_NUM + userId);
    }

    /**
     * 关注数量 保存
     */
    public static void setFollowNum(Long userId,int num) {
        getRedisTemplate().opsForValue().set(RedisConstantKey.FOLLOW_NUM + userId, num, 7, TimeUnit.DAYS);
    }

    /**
     * 获取关注数量
     */
    public static Integer getFollowNum(Long userId) {
        return (Integer) getRedisTemplate().opsForValue().get(RedisConstantKey.FOLLOW_NUM + userId);
    }

    /**
     * 删除关注数量
     */
    public static void deleteFollowNum(Long userId) {
        getRedisTemplate().delete(RedisConstantKey.FOLLOW_NUM + userId);
    }
}
