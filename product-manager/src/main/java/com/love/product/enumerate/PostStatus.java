package com.love.product.enumerate;

/**
 * @PackageName: com.love.product.enumerate
 * @Description: PostStatus
 * @Author: Administrator
 * @Date: 2023/7/3 11:03
 */

public enum PostStatus {
    ALL(0), //全部
    PENDING(1), //审核中
    REJECTED(2), //未通过
    PUBLISHED(3), //已发布
    UNPUBLISHED(4), //下架
    TRASHED(5); //回收站

    private final int value;

    private PostStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
