package com.love.product.config.exception;

/**
 * @author Administrator
 * @date 2022-10-19 12:00
 * @describe 异常封装
 */
public interface BaseError {

    /**
     * 错误码
     */
    Integer getResultCode();
    /**
     * 错误描述
     */
    String getResultMsg();

}
