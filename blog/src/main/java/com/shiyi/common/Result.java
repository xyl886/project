package com.shiyi.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.shiyi.common.ResultCode.*;

/**
 *  <p> 统一返回结果类 </p>
 *
 * @description :
 * @author :  blue
 */
@ApiModel(value = "统一返回结果类")
@Data
public class Result {
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "响应消息", required = false)
    private String message;

    /**
     * 响应码：参考`ResultCode`
     */
    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    /**
     * 响应中的数据
     */
    @ApiModelProperty(value = "响应数据", required = false)
    private Object data;

    @ApiModelProperty(value = "响应数据", required = false)
    private Map<String,Object> extra = new HashMap<>();

    public Result putExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }

    public static Result error(String message) {
        return new Result(FAILURE.getCode(), message, null);
    }

    public static Result error() {
        return new Result(FAILURE.getCode(), ERROR.getDesc(), null);
    }

    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }

    public static Result success() {
        return new Result(SUCCESS.getCode(), SUCCESS.getDesc(), null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS.getCode(),SUCCESS.getDesc(), data);
    }

    public static Result success(String message, Object data) {
        return new Result(SUCCESS.getCode(), message, data);
    }

    public static Result success(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result success(Integer code, String message) {
        return new Result(code, message,null);
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }
}
