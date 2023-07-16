package com.love.product.enumerate;

import lombok.Getter;

/**
 * @PackageName: com.love.product.enumerate
 * @Description: LikeStatus
 * @Author: Administrator
 * @Date: 2023/7/10 17:04
 */

@Getter
public enum LikeStatus {

    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞"),
    ;

    private final Integer code;

    private final String msg;

    LikeStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
