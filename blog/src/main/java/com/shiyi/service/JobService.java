package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Job;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.enums.TaskException;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobService extends IService<Job> {

    Result listJob(String jobName, String jobGroup, String status);

    Result getJobById(Long jobId);

    Result insertJob(Job job) throws SchedulerException, TaskException;

    Result updateJob(Job job) throws SchedulerException, TaskException;

    Result deleteJob(Long jobId) throws SchedulerException;

    Result deleteBatch(List<Long> ids);

    Result pauseJob(Job job) throws SchedulerException ;

    Result run(Job job);

    Result changeStatus(Job job) throws SchedulerException;

}
