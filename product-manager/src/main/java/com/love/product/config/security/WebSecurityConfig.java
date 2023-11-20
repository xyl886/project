package com.love.product.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security-配置类(认证服务器)
 *
 * 1.配置请求URL的访问策略
 * 2.自定义认证登录页面URL
 * 3.配置OAuth2密码模式
 *
 */

@EnableWebSecurity//开启权限验证
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)//通过表达式控制方法权限
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置访问策略
     *
     * @description    1.设置授权请求
     *                 2.自定义登录界面
     *                 3.设置使用jwt，可以允许跨域
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                // 放行
                .antMatchers(
                        "/system/login",
                        "/druid/**",
                        "/webSocket/**",
                        "/category/listAll",
                        "/login/code",
                        "/login/userLogin",
                        "/login/userRegister",
                        "/user/reset",
                        "/oauth/**",
                        "/actuator/**",
                        "/doc.html/**",
                        "/favicon.ico",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
                // 其他需要拦截
    }

    /**
     * 需要配置这个支持password模式 support password grant type
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
