package com.love.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @date 2022-10-19 10:35
 * @describe 性别
 */
@Getter
@AllArgsConstructor
public enum Gender {

    /**
     *
     */
    DUNNO(0, "保密"),
    MAN(1, "男"),
    WOMAN(2, "女"),
    ;

    /**
     * 枚举值
     */
    private final int value;

    /**
     * 枚举文本说明
     */
    private final String text;

    public static Gender valueOf(Integer value) {
        if (value != null) {
            for (Gender item : Gender.values()) {
                if (value == item.value) {
                    return item;
                }
            }
        }
        return null;
    }
}

