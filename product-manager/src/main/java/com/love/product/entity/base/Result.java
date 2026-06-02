package com.love.product.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回状态码： (200操作成功) (500操作失败)")
    protected Integer code;

    @ApiModelProperty(value = "前端toast展示的信息")
    protected String msg;

    @ApiModelProperty(value = "返回数据")
    protected T data;

    public Result(){
    }

    // ========== 成功 ==========

    public static <T> Result<T> OK() {
        return ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> OK(T data) {
        return ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> OK(String msg, T data) {
        return ok(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /** 仅带消息的成功响应（无 data） */
    public static <T> Result<T> OKMsg(String msg) {
        return ok(ResultCode.SUCCESS.getCode(), msg, null);
    }

    // ========== 失败 ==========

    public static <T> Result<T> fail() {
        return fail(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), null);
    }

    public static <T> Result<T> failMsg(String msg) {
        return fail(ResultCode.FAILED.getCode(), msg, null);
    }

    public static <T> Result<T> failMsg(Integer code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> Result<T> fail(String msg, T data) {
        return fail(ResultCode.FAILED.getCode(), msg, data);
    }

    public static <T> Result<T> fail(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // ========== 内部 helper ==========

    private static <T> Result<T> ok(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
