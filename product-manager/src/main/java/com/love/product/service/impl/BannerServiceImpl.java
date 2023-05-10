package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Banner;
import com.love.product.entity.base.Result;
import com.love.product.mapper.BannerMapper;
import com.love.product.service.BannerService;
import com.love.product.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    private FileUploadService fileUploadService;

    @Override
    public Result<List<Banner>> listAll() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Banner::getSort).orderByAsc(Banner::getCreateTime);
        List<Banner> banners = list(queryWrapper);
        banners.forEach(item-> item.setImgPath(fileUploadService.getImgPath(item.getImgPath())));
        return Result.OK(banners);
    }

    @Override
    public Result<List<Banner>> updateBanner() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Banner::getSort).orderByAsc(Banner::getCreateTime);
        List<Banner> banners = list(queryWrapper);
        banners.forEach(item-> item.setImgPath(fileUploadService.getImgPath(item.getImgPath())));
        return Result.OK(banners);
    }
}
