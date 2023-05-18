package com.love.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @PackageName: com.love.product
 * @Description: OssService
 * @Author: Administrator
 * @Date: 2023/4/28 11:23
 */

public interface OssService {
    String uploadFile(MultipartFile file) throws IOException;

    void delFile(String filePath);

    String getOssImgPath(String imgPath) ;
}
