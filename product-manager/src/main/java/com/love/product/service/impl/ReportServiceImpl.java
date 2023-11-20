package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Category;
import com.love.product.entity.Report;
import com.love.product.entity.Tags;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.ReportPageReq;
import com.love.product.entity.vo.CategoryVO;
import com.love.product.entity.vo.CommentVO;
import com.love.product.entity.vo.ReportVO;
import com.love.product.mapper.ReportMapper;
import com.love.product.service.FileUploadService;
import com.love.product.service.ReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: ReportServiceImpl
 * @Author: Administrator
 * @Date: 2023/8/8 14:58
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Resource
    private FileUploadService fileUploadService;
    public void validate(Report report){
        Report entity = baseMapper.selectOne(
                new LambdaQueryWrapper<Report>().eq(Report::getPostId,report.getPostId())
                .eq(Report::getReportUserId,report.getReportUserId()));
        Assert.isNull(entity,"你已经举报过了!");
    }
    @Override
    public Result insertFeedback(Report report) {
        validate(report);
        baseMapper.insert(report);
        return Result.OK();
    }

    @Override
    public ResultPage listFeedBack(ReportPageReq reportPageReq) {
        Page<ReportVO> Page = baseMapper.selectPageList(
                new Page<>(reportPageReq.getCurrentPage(), reportPageReq.getPageSize()), reportPageReq);
        List<ReportVO> list=new ArrayList<>();
        if (Page.getTotal() > 0) {
            list = Page.getRecords();
        }
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }

    @Override
    public Result deleteReport(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? Result.OK(): Result.failMsg("删除反馈失败");
    }

    @Override
    public Result deleteBatch(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.OK(): Result.failMsg("批量删除反馈失败");
    }


}
