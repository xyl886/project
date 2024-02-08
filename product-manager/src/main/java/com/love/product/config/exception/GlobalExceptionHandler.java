package com.love.product.config.exception;

import com.love.product.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @date 2022-10-19 11:56
 * @describe 全局异常封装返回
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常，程序员主动抛出BizException异常会被这个方法捕获
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result bizExceptionHandler(BizException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return Result.failMsg(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常（空指针异常会被这个方法捕获）
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return Result.fail();
    }

    /**
     * java异常异常
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result ExceptionHandler(Exception e) {
        log.error(" msg : " + e.getMessage(), e);
        return Result.failMsg(500, "操作异常");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("访问被拒绝");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMsg = "缺少必需的参数：" + ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }
}
