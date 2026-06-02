package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Report;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.ReportPageReq;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: ReportService
 * @Author: Administrator
 * @Date: 2023/8/8 14:38
 */

public interface ReportService extends IService<Report> {
    void insertFeedback(Report feedBack);

    ResultPage listFeedBack(ReportPageReq reportPageReq);

    void deleteBatch(List<Long> ids);

    void deleteReport(Long id);
}
