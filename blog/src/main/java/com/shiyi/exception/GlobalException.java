package com.shiyi.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.shiyi.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.shiyi.common.ResultCode.*;

/**
 * @author blue
 * @date 2022/3/11
 * @apiNote
 */
@ControllerAdvice(basePackages = "com.shiyi")
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result BusinessExceptionHandler(BusinessException ex) {
        if (ex.getCode() != -1) {
            logger.error("code : " + ex.getCode() + " msg : " + ex.getMessage(), ex);
        }
        if(StringUtils.isBlank(ex.getLocalizedMessage())||StringUtils.isBlank(ex.getMessage())){
            return Result.error(ERROR.getCode(), ERROR.getDesc());
        }
        return Result.error(ex.getCode(), ex.getMessage());
    }

    // Assert业务异常
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result AssertExceptionHandler(IllegalArgumentException ex) {
        logger.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return Result.error(ERROR.getCode(),ERROR.getDesc());
        }
        return Result.error(ex.getMessage());
    }

    // 登录异常
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Result NotLoginExceptionHandler(NotLoginException ex) {
        logger.error( " msg : " + ex.getMessage(), ex);
        return Result.error(NOT_LOGIN.getCode(),NOT_LOGIN.getDesc());
    }

    // 权限异常
    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Result NotPermissionExceptionHandler(NotPermissionException ex) {
        logger.error( " msg : " + ex.getMessage(), ex);
        return Result.error(NO_PERMISSION.getCode(),"无此权限：" + ex.getCode());
    }

    // java异常异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result ExceptionHandler(Exception ex) {
        logger.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return Result.error(ERROR.getCode(),ERROR.getDesc());
        }
        return Result.error(ex.getMessage());
    }
}
