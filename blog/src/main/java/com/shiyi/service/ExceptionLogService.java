package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.ExceptionLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-11
 */
public interface ExceptionLogService extends IService<ExceptionLog> {

    Result listExceptionLog();

    Result deleteBatch(List<Long> ids);
}
