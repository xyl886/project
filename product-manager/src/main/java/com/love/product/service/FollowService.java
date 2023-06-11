package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Follow;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FollowPageReq;
import com.love.product.entity.vo.FollowVO;

import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface FollowService extends IService<Follow> {

    Result<?> add(Long userId, Long beFollowedUserId,Integer deleted);

    Follow getDetail(Long userId, Long beFollowedUserId);

    int getFansNumByUserId(Long userId);

    int getFollowNumByUserId(Long userId);

    ResultPage<FollowVO> getPage(Long userId, FollowPageReq followPageReq);

    List<Long> getFollowedUserIdsByUserId(Long userId);
}
