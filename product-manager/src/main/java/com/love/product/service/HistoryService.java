package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.History;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.enumerate.PostsType;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: HistoryService
 * @Author: Administrator
 * @Date: 2023/4/24 11:17
 */

public interface HistoryService extends IService<History> {

    History findHistory(Long userId, Long id);

    void updateHistory(History existingHistory);

    void saveHistory(History history);

    ResultPage<HistoryVO> getPage(Long userId, PageQuery pageQuery);

    Result<?> del( Long id);
}
