package com.love.product.config.swagger;

import com.love.product.config.fileupload.FileUploadConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@SpringBootApplication
public class SwaggerBootstrapUiDemoApplication  implements WebMvcConfigurer {

    @Resource
    private FileUploadConfig fileUploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler(fileUploadConfig.getImageMapperPath() + "**").addResourceLocations("file:" + fileUploadConfig.getImageRealPath());
    }
}
