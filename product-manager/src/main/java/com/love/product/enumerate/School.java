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
    GUAN_TANG(1, "官塘校区"),
    SHE_WAN(2, "社湾校区"),
    ;

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

