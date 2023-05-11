package com.love.product.enumerate;

/**
 * @author Administrator
 * @date 2022-10-19 10:35
 * @describe 性别
 */

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
    private int value;

    /**
     * 枚举文本说明
     */
    private String text;

    Gender(int value, String text) {
        this.value = value;
        this.text = text;
    }

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

