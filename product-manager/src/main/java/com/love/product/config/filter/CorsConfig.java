package com.love.product.config.filter;

import com.love.product.interceptor.PaginationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.filter
 * @Description: CorsConfig
 * @Author: Administrator
 * @Date: 2023/3/17 21:06
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Resource
    private PaginationInterceptor paginationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paginationInterceptor);
    }
}
