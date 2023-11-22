package com.love.product.quartz.controller;

import com.love.product.entity.base.Result;

import com.love.product.entity.base.ResultPage;
import com.love.product.quartz.domain.SysJobLog;
import com.love.product.quartz.service.SysJobLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/jobLog")
public class SysJobLogController
{
    @Resource
    private SysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @GetMapping("/list")
    public ResultPage list(SysJobLog sysJobLog)
    {

        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return (ResultPage) ResultPage.OK(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJobLog sysJobLog)
    {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
//        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
//        util.exportExcel(response, list, "调度日志");
    }

    /**
     * 根据调度编号获取详细信息
     */
    @GetMapping(value = "/{jobLogId}")
    public Result getInfo(@PathVariable Long jobLogId)
    {
        return Result.OK(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     */
    @DeleteMapping("/{jobLogIds}")
    public Result remove(@PathVariable Long[] jobLogIds)
    {
        return Result.OK(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @DeleteMapping("/clean")
    public Result clean()
    {
        jobLogService.cleanJobLog();
        return Result.OK();
    }
}
