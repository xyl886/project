package com.love.product;


import com.love.product.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class OssController {

    @Resource
    private OssService ossService;

    //oss对象存储
    @PostMapping("add")
    public String uploadOssFile(MultipartFile file) throws Exception{
        //获取上传文件 MultipartFile
        //返回图片在oss上的路径
        return ossService.uploadFile(file);
    }

}
