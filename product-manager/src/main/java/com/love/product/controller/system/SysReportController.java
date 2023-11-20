package com.love.product.controller.system;

import com.love.product.entity.base.Result;
import com.love.product.entity.Report;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.ReportPageReq;
import com.love.product.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysReportController
 * @Author: Administrator
 * @Date: 2023/8/7 10:47
 */
@RestController
@RequestMapping("/system/report")
@Api(tags = "反馈管理")
public class SysReportController {
    @Resource
    private ReportService reportService;
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加反馈", httpMethod = "POST", response = Result.class, notes = "添加反馈")
    public Result addFeedback(@RequestBody Report feedBack) {
        return  reportService.insertFeedback(feedBack);
    }
    @PostMapping(value = "/list")
    @ApiOperation(value = "反馈列表", httpMethod = "POST", response = Result.class, notes = "反馈列表")
    public ResultPage list(@RequestBody ReportPageReq reportPageReq) {
        return reportService.listFeedBack(reportPageReq);
    }
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除反馈", httpMethod = "POST", response = Result.class, notes = "修改文章")
    public Result delete(Long id) {
        return reportService.deleteReport(id);
    }
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "删除反馈", httpMethod = "DELETE", response = Result.class, notes = "删除反馈")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        return reportService.deleteBatch(ids);
    }

}
