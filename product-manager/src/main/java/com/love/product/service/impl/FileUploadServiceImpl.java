package com.love.product.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.love.product.config.Exception.BizException;
import com.love.product.config.fileupload.FileUploadConfig;
import com.love.product.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2022-12-27 14:44
 * @describe 文件上传
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private FileUploadConfig fileUploadConfig;

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null) {
            throw new BizException("图片不能为空");
        }

        //得到上传文件的文件名
        String fileName = file.getOriginalFilename();
        //以传入的字符串开头，到该字符串的结尾，前开后闭
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        long size = file.getSize();
        double mul = NumberUtil.div(size, (1024 * 1024), 2);
        // 自定义异常
        if (mul > 2) {
            throw new BizException("图片大小不能大于2M");
        }
        if (!isImage(suffixName)) {
            throw new BizException("不是图片格式");
        }
        // 这里可以用uuid等 拼接新图片名
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
        // 创建路径
        String destFileName = fileUploadConfig.getImageRealPath() + File.separator + newFileName;
        File destFile = new File(destFileName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        try {
            //将图片保存到文件夹里
            file.transferTo(new File(destFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("图片上传错误");
        }
        //返回相对路径存储
        return fileUploadConfig.getImageMapperPath() + newFileName;
    }

    /**
     * 传进 .jpg  类似的格式 判断是否时图片格式
     * @param suffixName 图片格式后缀
     * @return
     */
    public static boolean isImage(String suffixName) {
        List<String> strings = Arrays.asList(".webp", ".png", ".jpg", ".jif", ".jpeg");
        return strings.contains(suffixName);
    }
    /**
     *本地存储获取URL
     */
    @Override
    public String getImgPath(String imgPath){
        if(StringUtils.isEmpty(imgPath)){
            return imgPath;
        }
        return fileUploadConfig.getHostIp() + imgPath;
    }
}
