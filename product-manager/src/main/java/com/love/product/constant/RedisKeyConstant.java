package com.love.product.constant;

/**
 * Redis Key 常量
 * <pre>
 * 命名规范: 模块:业务:标识
 *
 * user:info:1611899...        → 用户信息缓存
 * email:code:xxx@qq.com       → 邮箱验证码
 * image:captcha:uuid          → 图形验证码
 * limit:captcha:192.168.1.1   → 接口限流
 * follow:fans:1611899...      → 粉丝数
 * follow:following:1611899... → 关注数
 * like:status                 → 点赞状态哈希 {userId:postId → 0/1}
 * like:count                  → 点赞计数哈希 {postId → 123}
 * </pre>
 */
public class RedisKeyConstant {

    /**
     * 接口限流
     */
    public static final String RATE_LIMIT = "limit:";

    /**
     * 用户信息缓存
     */
    public static final String USER_INFO = "user:info:";

    /**
     * 邮箱验证码
     */
    public static final String EMAIL_CODE = "email:code:";

    /**
     * 图形验证码
     */
    public static final String IMAGE_CAPTCHA = "image:captcha:";

    /**
     * 粉丝数
     */
    public static final String FOLLOW_FANS = "follow:fans:";

    /**
     * 关注数
     */
    public static final String FOLLOW_FOLLOWING = "follow:following:";

    /**
     * 点赞状态哈希
     */
    public static final String LIKE_STATUS = "like:status";

    /**
     * 点赞计数哈希
     */
    public static final String LIKE_COUNT = "like:count";

}
