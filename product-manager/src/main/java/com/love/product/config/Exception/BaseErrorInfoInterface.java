package com.love.product.config.Exception;

/**
 * @author hjf
 * @date 2022-10-19 12:00
 * @describe 异常封装
 */
public interface BaseErrorInfoInterface {

    /**
     * 错误码
     */
    Integer getResultCode();
    /**
     * 错误描述
     */
    String getResultMsg();

}
