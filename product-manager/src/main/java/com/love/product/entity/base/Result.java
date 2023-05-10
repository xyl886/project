package com.love.product.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    @ApiModelProperty(value = "是否重新获取token")
    protected Boolean refreshTokenFlag;

    @ApiModelProperty(value = "登录刷新token")
    public String refreshToken;

    public Result(){
        checkRefreshToken(this);
    }

    /**
     * 是否需要重新获取token 并且返回新的token
     *
     * @return true/false
     */
    public static void checkRefreshToken(Result result){
        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null){
            HttpServletResponse response = servletRequestAttributes.getResponse();
            if(response == null){
                return;
            }
            boolean isRefresh = false;
            String refreshToken = null;
            String isRefreshToken = response.getHeader("isRefreshToken");
            if(isRefreshToken != null && isRefreshToken.equals("yes")){
                isRefresh = true;
                refreshToken = response.getHeader("access_token");
            }
            result.setRefreshTokenFlag(isRefresh);
            result.setRefreshToken(refreshToken);
        }
    }

    public static <T> Result<T> OK() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> OK(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> OK(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> OK(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> OKMsg(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> OK(Integer code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> OKMsg(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回单参数（比如String、Long等）时，如不确定后续会不会增加参数，则尽量使用该方法
     * @param key 返回单参数的key
     * @param data 返回单参数的值
     */
    public static <T> Result<Map<String, T>> OKMap(String key, T data) {
        Map<String, T> map = new HashMap<>();
        map.put(key, data);

        Result<Map<String, T>> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setData(map);
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(ResultCode.FAILED.getMsg());
        return result;
    }

    public static <T> Result<T> fail(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> failMsg(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> failMsg(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Integer code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(ResultCode.FAILED.getMsg());
        return result;
    }

    public static <T> Result<T> failMsg(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(ResultCode.FAILED.getMsg());
        result.setData(data);
        return result;
    }

}
