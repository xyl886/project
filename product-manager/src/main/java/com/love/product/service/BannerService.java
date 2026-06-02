package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface BannerService extends IService<Banner> {

    List<Banner> listAll();

    List<Banner> updateBanner(MultipartFile file, Long id) throws IOException;

    List<Banner> add(MultipartFile file);

    void deleteBanner(Long id);

    void deleteBatch(List<Long> ids);
}
