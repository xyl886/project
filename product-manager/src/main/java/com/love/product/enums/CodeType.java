package com.love.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeType {

    /**
     *
     */
    LOGIN("login", "登录"),

    REGISTER("register", "注册"),

    RESET("forget", "密码重置"),
    ;

    /**
     * 枚举值
     */
    private final String type;

    /**
     * 枚举文本说明
     */
    private final String text;

    public static String getType(String type) {
        for (CodeType value : CodeType.values()) {
            if (value.getType().equals(type)) {
                return value.getText();
            }
        }
        return null;
    }
}
