package com.love.product.enumerate;

/**
 * @author Administrator
 * @date 2022-10-19 10:35
 * @describe 校区 1官塘校区 2社湾校区
 */

public enum School {

    /**
     *
     */
    STUDY(1,"学习"),
    LIFE(2,"生活"),
    ENTERTAINMENT(3,"娱乐"),
    HELP(4,"求助"),
    EMPLOYMENT(5,"就业"),
    NEWS_ANNOUNCEMENT(6,"新闻/公告"),
    MY_SHARE(7,"我的分享"),
    MY_FOLLOW(8,"我的关注");
    /**
     * 枚举值
     */
    private int value;

    /**
     * 枚举文本说明
     */
    private String text;

    School(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static School valueOf(Integer value) {
        if (value != null) {
            for (School item : School.values()) {
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

