package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Collect;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CollectPageReq;
import com.love.product.entity.vo.CollectVO;

import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface CollectService extends IService<Collect> {

    Result<?> add(Long userId, Integer deleted, Long... postsIds);

    Collect getDetail(Long userId, Long postsId);

    ResultPage<CollectVO> getPage(Long userId, CollectPageReq pageQuery);

    Result<?> deleteBatch(Long userId, List<Long> postsIds);
}
