package com.love.product.enumerate;

/**
 * @author hjf
 * @date 2022-10-19 10:35
 * @describe 帖子类型 1闲置帖 2校园帖
 */

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
    private int value;

    /**
     * 枚举文本说明
     */
    private String text;

    PostsType(int value, String text) {
        this.value = value;
        this.text = text;
    }

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

