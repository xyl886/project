package com.love.product.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器-配置类
 *
 * 1.设置接口访问权限
 * 2.token验证
 *
 */
@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${token.resourceId}")
    private String resourceId;

    /**
     * 定义资源服务器接口访问权限
     *
     * 1.定义无权限接口
     * 2.定义接口访问权限为admin
     * 3.定义接口访问权限为sys_admin
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/getToken",
                        "/parseToken",
                        "/login/userLogin",
                        "/login/userRegister",
                        "/posts/getPage",
                        "/posts/getDetail",
                        "/posts/browse",
                        "/banner/listAll",
                        "/image/**",//图片
                        "/js/**",
                        "/doc.html/**",
                        "/favicon.ico",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();

    }

    /**
     * 定义资源服务器解析协议表头（需要与认证服务器定义的表头一致）
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId).stateless(true);
        resources.authenticationEntryPoint(new OauthInterceptor());
    }


}
