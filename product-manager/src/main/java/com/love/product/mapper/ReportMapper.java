package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.Report;
import com.love.product.entity.req.ReportPageReq;
import com.love.product.entity.vo.ReportVO;

/**
 * @PackageName: com.love.product.mapper
 * @Description: ReportMapper
 * @Author: Administrator
 * @Date: 2023/8/8 14:59
 */

public interface ReportMapper extends BaseMapper<Report> {
    Page<ReportVO> selectPageList(Page<Object> objectPage, ReportPageReq reportPageReq);
}
