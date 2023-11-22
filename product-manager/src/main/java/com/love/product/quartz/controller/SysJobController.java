package com.love.product.quartz.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.quartz.domain.SysJob;
import com.love.product.quartz.exception.TaskException;
import com.love.product.quartz.service.SysJobService;
import com.love.product.quartz.util.CronUtils;
import com.love.product.quartz.util.ScheduleUtils;
import com.love.product.util.JwtUtil;
import com.love.product.util.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.love.product.constant.CommonConstant.*;


/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/job")
public class SysJobController {
    @Resource
    private SysJobService jobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    public ResultPage<?> list(SysJob sysJob) {

        List<SysJob> list = jobService.selectJobList(sysJob);
        return (ResultPage<?>) ResultPage.OK(list);
    }

    /**
     * 导出定时任务列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
//        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
//        util.exportExcel(response, list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "/{jobId}")
    public Result getInfo(@PathVariable("jobId") Long jobId)
    {
        return Result.OK(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @PostMapping
    public Result add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), LOOKUP_RMI))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { LOOKUP_LDAP, LOOKUP_LDAPS }))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { HTTP, HTTPS }))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), JOB_ERROR_STR))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
        {
            return Result.failMsg("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setCreateBy(String.valueOf(JwtUtil.getUserId()));
        return Result.OK(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @PutMapping
    public Result edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), LOOKUP_RMI))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { LOOKUP_LDAP, LOOKUP_LDAPS }))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { HTTP, HTTPS }))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), JOB_ERROR_STR))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
        {
            return Result.failMsg("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setUpdateBy(String.valueOf(JwtUtil.getUserId()));
        return Result.OK(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public Result changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return Result.OK(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public Result run(@RequestBody SysJob job) throws SchedulerException
    {
        boolean result = jobService.run(job);
        return result ? Result.OK() : Result.failMsg("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobIds}")
    public Result remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return Result.OK();
    }
}
