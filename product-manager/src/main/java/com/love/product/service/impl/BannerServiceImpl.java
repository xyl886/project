package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.Banner;
import com.love.product.mapper.BannerMapper;
import com.love.product.service.BannerService;
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
    private OssService ossService;

    @Override
    public List<Banner> listAll() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Banner::getSort).orderByAsc(Banner::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public List<Banner> updateBanner(MultipartFile file, Long id) {
        String imgPath;
        try {
            imgPath = ossService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Banner banner = Banner.builder().imgPath(imgPath).build();
        banner.setId(id);
        baseMapper.updateById(banner);
        return this.listAll();
    }

    @Override
    public List<Banner> add(MultipartFile file) {
        String imgPath;
        try {
            imgPath = ossService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Banner banner = Banner.builder().imgPath(imgPath).build();
        baseMapper.insert(banner);
        return this.listAll();
    }

    @Override
    public void deleteBanner(Long id) {
        int rows = baseMapper.deleteById(id);
        if (rows <= 0) {
            throw new BizException("删除失败！");
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        removeByIds(ids);
    }


}
