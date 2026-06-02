package com.love.product.util;

import cn.dev33.satoken.stp.StpUtil;

/**
 * @author Administrator
 * @date 2022-11-03 16:51
 * @describe 基于 Sa-Token 的鉴权工具类
 */

public class JwtUtil {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        try {
            return Long.parseLong(StpUtil.getLoginId().toString());
        } catch (Exception e) {
            return null;
        }
    }

}
