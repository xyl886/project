package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.FeedBack;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
public interface FeedBackService extends IService<FeedBack> {

    Result listFeedBack(Integer type);

    Result deleteBatch(List<Integer> ids);


    Result insertFeedback(FeedBack feedBack);

}
