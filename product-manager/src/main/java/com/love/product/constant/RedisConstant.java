package com.love.product.constant;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2023-01-03 16:09
 * @describe redis key 常量
 */

public class RedisConstant implements Serializable {
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
