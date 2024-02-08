package com.love.product.enums;

import com.love.product.service.PostsService;
import com.love.product.util.JwtUtil;
import lombok.Getter;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @PackageName: com.love.product.enumerate
 * @Description: LikeStatus
 * @Author: Administrator
 * @Date: 2023/7/10 17:04
 */

@Getter
public enum LikeStatus {

    LIKE(0, "点赞"),
    UNLIKE(1, "取消点赞/未点赞"),
    NOT_EXIST(2, "不存在点赞信息");


    private final Integer code;

    private final String msg;

    LikeStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Resource
    private PostsService postsService;

    public HashMap<String, Object> setHashValue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", this.code);
        map.put("updateTime", System.currentTimeMillis());
        return map;
    }
}
