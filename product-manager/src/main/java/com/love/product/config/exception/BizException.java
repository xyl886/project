package com.love.product.config.exception;

import com.love.product.entity.base.ResultCode;

/**
 * @author Administrator
 * @date 2022-10-19 11:55
 * @describe 自定义异常处理类
 */

public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    protected Integer errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(BaseError baseError) {
        super(String.valueOf(baseError.getResultCode()));
        this.errorCode = baseError.getResultCode();
        this.errorMsg = baseError.getResultMsg();
    }

    public BizException(BaseError baseError, Throwable cause) {
        super(String.valueOf(baseError.getResultCode()), cause);
        this.errorCode = baseError.getResultCode();
        this.errorMsg = baseError.getResultMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(Integer errorCode, String errorMsg) {
        super(String.valueOf(errorCode));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public BizException(ResultCode resultCode) {
        super(String.valueOf(resultCode));
        this.errorCode = resultCode.getCode();
        this.errorMsg = resultCode.getMsg();
    }
    public BizException(Integer errorCode, String errorMsg, Throwable cause) {
        super(String.valueOf(errorCode), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
