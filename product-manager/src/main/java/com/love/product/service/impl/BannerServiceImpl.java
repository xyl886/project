package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Banner;
import com.love.product.entity.base.Result;
import com.love.product.mapper.BannerMapper;
import com.love.product.service.BannerService;
import com.love.product.service.FileUploadService;
import com.love.product.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private OssService ossService;

    @Override
    public Result<List<Banner>> listAll() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Banner::getSort).orderByAsc(Banner::getCreateTime);
        List<Banner> banners = list(queryWrapper);
//        banners.forEach(item-> item.setImgPath(item.getImgPath()));
        return Result.OK(banners);
    }

    @Override
    public Result<List<Banner>> updateBanner(MultipartFile file, Long id) {
        String imgPath;
        try {
            imgPath = ossService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Banner banner = Banner.builder().imgPath(imgPath).build();
        banner.setId(id);
        baseMapper.updateById(banner);
        return Result.OK(this.listAll().getData());
    }

    @Override
    public Result add(MultipartFile file) {
        String imgPath;
        try {
            imgPath = ossService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Banner banner = Banner.builder().imgPath(imgPath).build();
        baseMapper.insert(banner);
        return Result.OK(this.listAll().getData());
    }

    @Override
    public Result deleteBanner(Long id) {
        baseMapper.deleteById(id);
        return Result.OKMsg("删除成功！");
    }

    @Override
    public Result deleteBatch(List<Long> ids) {
        return null;
    }


}
