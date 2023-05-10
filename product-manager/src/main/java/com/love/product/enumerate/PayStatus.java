package com.love.product.enumerate;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 支付状态 1待支付 2待发货 3已发货 4已完成 5已取消
 */
public enum PayStatus {
    /**
     *
     */
    WAIT_PAY(1, "待支付"),
    WAIT_SEND(2, "待发货"),
    HAD_SEND(3, "已发货"),
    FINISHED(4, "已完成"),
    PAY_CANCEL(5, "已取消"),
    ;

    /**
     * 枚举值
     */
    private int value;

    /**
     * 枚举文本说明
     */
    private String text;

    PayStatus(int value, String text) {
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

    public static PayStatus valueOf(int value) {
        for (PayStatus item : PayStatus.values()) {
            if (value == item.value) {
                return item;
            }
        }
        return null;
    }
}
