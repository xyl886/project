package com.love.product.config.fileupload;

/**
 * @PackageName: com.love.product.config.fileupload
 * @Description: AliyunOSSConfig
 * @Author: Administrator
 * @Date: 2023/4/28 9:26
 */

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("aliyun.oss") //指定配置文件
public class AliyunOSSConfig implements InitializingBean {
    //读取配置文件的内容

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    //定义公共静态常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() {
        END_POINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
    }
}
