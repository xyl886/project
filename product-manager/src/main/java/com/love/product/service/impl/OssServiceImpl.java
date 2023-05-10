package com.love.product.service.impl;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: OssServiceImpl
 * @Author: Administrator
 * @Date: 2023/4/28 11:25
 */

import cn.hutool.core.util.NumberUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.love.product.config.Exception.BizException;
import com.love.product.config.fileupload.AliyunOSS;
import com.love.product.entity.base.Result;
import com.love.product.service.OssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFile(MultipartFile file) {

        String endpoint = AliyunOSS.END_POINT;
        String accessKeyId = AliyunOSS.ACCESS_KEY_ID;
        String accessKeySecret = AliyunOSS.ACCESS_KEY_SECRET;
        String bucketName = AliyunOSS.BUCKET_NAME;
        String url;
        if (file == null) {
            throw new BizException("图片不能为空");
        }
        //得到上传文件的文件名
        String fileName = file.getOriginalFilename();
        //以传入的字符串开头，到该字符串的结尾，前开后闭
        String suffixName = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
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
        //创建OSSClient实例
        OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //上传文件到OSS
        try (InputStream inputStream = file.getInputStream()) {
            // meta设置请求头,解决访问图片地址直接下载
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/jpg");
            ossClient.putObject(bucketName, newFileName, inputStream, meta);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("图片上传错误");
        }
        //拼接访问URL
        url = "https://" + bucketName + "." + endpoint + "/" + newFileName;
        //关闭OSSClient
        ossClient.shutdown();
        return url;
    }
/**
 * 删除oss图片
 *
 * @return
 */
    public Result<?> delFile(String filePath) {
        String endpoint = AliyunOSS.END_POINT;
        String accessKeyId = AliyunOSS.ACCESS_KEY_ID;
        String accessKeySecret = AliyunOSS.ACCESS_KEY_SECRET;
        String bucketName = AliyunOSS.BUCKET_NAME;
        OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        boolean exist = ossClient.doesObjectExist(bucketName, filePath);
        if (!exist) {
            return Result.failMsg("图片不存在！");
        }
        ossClient.deleteObject(bucketName, filePath);
        ossClient.shutdown();
        return Result.OK("图片删除成功！");
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
     *oss对象存储获取URL
     */
    @Override
    public String getOssImgPath(String imgPath){
        StringUtils.isEmpty(imgPath);
        return imgPath;
    }
}
