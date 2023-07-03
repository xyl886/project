package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.AdminLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  操作日志服务类
 *
 * @author blue
 * @since 2021-11-10
 */
public interface AdminLogService extends IService<AdminLog> {

    /**
     * 分页查询操作日志
     * @return
     */
    Result listAdminLog();

    /**
     * 批量删除操作日志
     * @param ids 操作日志id集合
     * @return
     */
    Result deleteAdminLog(List<Long> ids);
}
