package com.love.product.enumerate;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 是否: 0否  1是 / 逻辑删除标记: 0正常  1已删除  / 是否已读 0否  1是  /  是否禁用 0否  1是
 */
public enum YesOrNo {
    /**
     *
     */
    NO(0, "否"),
    YES(1, "是"),
    ;

    /**
     * 枚举值
     */
    private int value;

    /**
     * 枚举文本说明
     */
    private String text;

    YesOrNo(int value, String text) {
        this.value = value;
        this.text = text;
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
    public static YesOrNo fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        return value == 1 ? YesOrNo.YES : YesOrNo.NO;
    }
    public static YesOrNo valueOf(int value) {
        for (YesOrNo item : YesOrNo.values()) {
            if (value == item.value) {
                return item;
            }
        }
        return null;
    }
}
