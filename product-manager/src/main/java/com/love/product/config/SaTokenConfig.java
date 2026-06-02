package com.love.product.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch("/login/**")
                    .notMatch("/system/login")
                    .notMatch("/druid/**")
                    .notMatch("/category/listAll")
                    .notMatch("/posts/listHot")
                    .notMatch("/posts/getPage")
                    .notMatch("/posts/getDetail")
                    .notMatch("/posts/browse")
                    .notMatch("/banner/listAll")
                    .notMatch("/banner/add")
                    .notMatch("/postsComment/listByPostsId")
                    .notMatch("/home/lineCount")
                    .notMatch("/image/**")
                    .notMatch("/js/**")
                    .notMatch("/doc.html/**")
                    .notMatch("/favicon.ico")
                    .notMatch("/webjars/**")
                    .notMatch("/swagger-resources/**")
                    .notMatch("/v2/api-docs/**")
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
}
