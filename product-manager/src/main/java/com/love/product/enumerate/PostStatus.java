package com.love.product.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @PackageName: com.love.product.enumerate
 * @Description: PostStatus
 * @Author: Administrator
 * @Date: 2023/7/3 11:03
 */
@Getter
@AllArgsConstructor
public enum PostStatus {
    ALL(0,""), //全部
    PENDING(1,"审核中"), //审核中
    REJECTED(2,"未通过"), //未通过
    PUBLISHED(3,"已发布"), //已发布
    UNPUBLISHED(4,"已下架"), //下架
    TRASHED(5,"已删除"); //回收站

    /**
     * 枚举值
     */
    private final int value;

    /**
     * 枚举文本说明
     */
    private final String text;


    public static PostStatus valueOf(Integer value) {
        if (value != null) {
            for (PostStatus item : PostStatus.values()) {
                if (value == item.value) {
                    return item;
                }
            }
        }
        return null;
    }
}
