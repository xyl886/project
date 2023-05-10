package com.love.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjf
 * @date 2022-12-27 14:44
 * @describe 文件上传
 */
public interface FileUploadService {

    String uploadImage(MultipartFile file);

    String getImgPath(String imgPath);

}
