package com.love.product.config.fileupload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("file-upload-config")
public class FileUploadConfig {


	private String imageRealPath;//图片存储路径

	private String imageMapperPath;//图片的映射路径

	private String hostIp;//后端地址

	private String defaultAvatar;//默认头像

	private String defaultNickname;//默认昵称
}
