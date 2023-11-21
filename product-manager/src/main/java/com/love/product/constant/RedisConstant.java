package com.love.product.constant;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2023-01-03 16:09
 * @describe redis key 常量
 */

public class RedisConstant implements Serializable {

    public static final String POSTS = "posts:";
    public static final String HOTLIST = "hotList";
    public static final String POST_TITLES = "posts_titles";
    /**
     * 用户信息
     */
    public static final String USER_USERINFO = "user:userinfo:";
    /**
     * 验证码
     */
    public static final String CODE = "code:";
    public static final String REFRESH_TOKEN = "refresh_token:";
    /**
     * 图形验证码
     */
    public static final String IMAGE_CODE = "imageCode:";
    /**
     * 关注
     */
    public static final String FOLLOW = "follow";
    /**
     * 用户粉丝数量
     */
    public static final String FANS_NUM = RedisConstant.FOLLOW + "fans_num:";

    /**
     * 用户关注数量
     */
    public static final String FOLLOW_NUM = RedisConstant.FOLLOW + "follow_num:";
}
