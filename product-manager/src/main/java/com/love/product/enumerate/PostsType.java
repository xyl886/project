package com.love.product.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @date 2022-10-19 10:35
 * @describe 帖子类型 1闲置帖 2校园帖
 */
@Getter
@AllArgsConstructor
public enum PostsType {

    /**
     *
     */
    LEAVE(1, "闲置帖"),
    SCHOOL(2, "校园帖"),
    ;

    /**
     * 枚举值
     */
    private final int value;

    /**
     * 枚举文本说明
     */
    private final String text;


    public static PostsType valueOf(Integer value) {
        if (value != null) {
            for (PostsType item : PostsType.values()) {
                if (value == item.value) {
                    return item;
                }
            }
        }
        return null;
    }
}

