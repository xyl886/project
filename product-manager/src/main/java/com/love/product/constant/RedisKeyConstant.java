package com.love.product.constant;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2023-01-03 16:09
 * @describe redis key 常量
 */

public class RedisKeyConstant implements Serializable {

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
    public static final String FANS_NUM = RedisKeyConstant.FOLLOW + "fans_num:";

    /**
     * 用户关注数量
     */
    public static final String FOLLOW_NUM = RedisKeyConstant.FOLLOW + "follow_num:";

    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";

    //保存用户被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。格式 222222::333333
     * @param UserId 被点赞的人id
     * @param PostId 点赞的人的id
     * @return
     */public static String getLikedKey(Long UserId, Long PostId){
        return UserId + "::" + PostId;
    }
}
