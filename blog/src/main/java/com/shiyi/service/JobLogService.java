package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobLogService extends IService<JobLog> {

    Result listJobLog(String jobName, String jobGroup, String status, String startTime,
                      String endTime, Long jobId);

    Result deleteBatch(List<Long> ids);

    Result clean();
}
