package com.love.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @PackageName: com.love.product.enumerate
 * @Description: Role
 * @Author: Administrator
 * @Date: 2023/6/14 14:52
 */
@Getter
@AllArgsConstructor
public enum Role {
    VISITOR(1,"游客"),
    STUDENT(2,"学生"),
    MANAGER(3,"管理员"),
            ;
    /**
     * 枚举值
     */
    private final Integer value;

    /**
     * 枚举文本说明
     */
    private final String text;


    public static Role valueOf(Integer value) {
        if (value != null) {
            for (Role item : Role.values()) {
                if (value.equals(item.value)) {
                    return item;
                }
            }
        }
        return null;
    }
}
