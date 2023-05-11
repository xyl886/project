package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Banner;
import com.love.product.entity.base.Result;

import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface BannerService extends IService<Banner> {

    Result<List<Banner>> listAll();

    Result<List<Banner>> updateBanner();
}
