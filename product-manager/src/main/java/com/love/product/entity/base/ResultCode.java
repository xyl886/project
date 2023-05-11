package com.love.product.entity.base;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 返回结果集code枚举
 */
public enum ResultCode {
    /**
     *
     */
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败，请重试")
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码含义
     */
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResultCode valueOf(int code) {
        for (ResultCode item : ResultCode.values()) {
            if (code == item.code) {
                return item;
            }
        }
        return null;
    }
}
