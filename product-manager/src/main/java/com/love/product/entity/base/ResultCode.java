package com.love.product.entity.base;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 返回结果集code枚举
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),

    FAILED(500, "操作失败，请重试"),

    EMAIL_ERROR(-10, "邮箱格式不对，请检查后重试!"),
    ERROR_EXCEPTION_MOBILE_CODE(10003, "验证码不正确或已过期，请重新输入"),
    PARAMS_ILLEGAL(10018, "参数不合法!!"),
    ROLE_IS_EXIST(10019, "该角色已存在！"),
    CATEGORY_IS_EXIST(10020, "该分类名称已存在!");

    private final int code;

    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
