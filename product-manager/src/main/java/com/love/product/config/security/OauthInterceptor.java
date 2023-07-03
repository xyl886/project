package com.love.product.config.security;

import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.UserInfoMapper;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * OAuth2异常拦截类
 *
 * 1.对oauth错误异常进行拦截，这里主要针对令牌过期进行处理
 * 2.新的令牌与刷新令牌的存储
 * 3.载入用户信息到spring Security的ContextHolder中，保证后续url转发
 * 4.刷新令牌过期后的返回状态
 *
 */
@Slf4j
public class OauthInterceptor extends OAuth2AuthenticationEntryPoint {

    @Value("${server.port}")
    private String port;

    @Resource
    private TokenConfig tokenConfig;

    /**
     * 在启动类中注入了restTemplate Bean
     */
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisService redisService;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private AuthenticationManager authenticationManager;

    private final WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try {
            ResponseEntity<?> result = exceptionTranslator.translate(authException);
            String resultString = Objects.requireNonNull(result.getBody()).toString();

            String uri = request.getRequestURI();
            log.info("uri:{}", uri);
            log.info("请求方法：{}", request.getMethod());

            if(result.getStatusCodeValue() != 200 && (resultString == null || !resultString.contains("Access token expired"))){
                if(result.getStatusCodeValue() == 401){
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    response.getWriter().print("{\"code\":403,\"msg\":\"请登录\"}");
                    response.getWriter().flush();
                    return;
                }
                response.setHeader("Content-Type", "application/json;charset=utf-8");
                response.getWriter().print("{\"code\":" + result.getStatusCodeValue() + ",\"msg\":\"" + result.getBody() + "\"}");
                response.getWriter().flush();
                return;
            }

            //判断是否为"访问令牌过期",如果不是则以默认的方法继续处理其他异常
            if (resultString != null && resultString.contains("Access token expired")) {
                //根据访问令牌，解析出当前令牌用户的用户名称，密码等信息
                String localUser = JwtToken.parseToken(request.getHeader("Authorization"), tokenConfig.getSigningKey());
                if(localUser == null){
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    response.getWriter().print("{\"code\":403,\"msg\":\"token缺失\"}");
                    response.getWriter().flush();
                    return;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> userMap = (Map<String, Object>) JsonUtil.json2Map(localUser);
                if(userMap == null){
                    // 获取刷新令牌失败时（刷新令牌过期时），返回指定格式的错误信息
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    response.getWriter().print("{\"code\":403,\"msg\":\"请登录\"}");
                    response.getWriter().flush();
                    return;
                }
                String email = (String)userMap.get("email");
                Long userId = (Long)userMap.get("userId");

                //根据用户名称，从数据库获取用户的刷新令牌 todo redis
                String refresh_token = (String) redisService.get("refresh_token:" + userId);
                if(refresh_token != null){
                    //获取当前用户信息
                    UserInfoVO userInfoVO  = userInfoService.getByEmail(email);

                    //获取OAuth2框架的配置信息，用于访问刷新令牌接口
                    Map<String, String> tokenMap = tokenConfig.getConfig();
                    Map<String,String> mapParam = new HashMap<>();
                    mapParam.put("email", userInfoVO.getEmail());
                    mapParam.put("password", userInfoVO.getOriginalPassword());
                    mapParam.put("client_id", tokenMap.get("clientId"));
                    mapParam.put("client_secret", tokenMap.get("secret"));
                    mapParam.put("grant_type", "refresh_token");//这里没有写错 采用刷新令牌的方式
                    mapParam.put("refresh_token", refresh_token);
                    try {

                        @SuppressWarnings("unchecked")
                        Map<String, String> mapResult = restTemplate
                                .getForObject(
                                        "http://localhost:"+port+"/oauth/token?username={email}&password={password}&client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}&refresh_token={refresh_token}",
                                        Map.class, mapParam);
                        if(mapResult != null){
                            // 如果刷新成功 跳转到原来需要访问的页面
                            //写入用户信息到redis，写入信息到SecurityContext中
                            redisService.set("user:userinfo:" + userInfoVO.getId(),userInfoVO);
                            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                                    userInfoVO.getEmail(), userInfoVO.getOriginalPassword(), grantedAuthorityList);
                            Authentication authentications = authenticationManager
                                    .authenticate(authRequest);
                            SecurityContextHolder.getContext().setAuthentication(
                                    authentications);

                            response.setHeader("access_token",
                                    mapResult.get("access_token"));
//                          response.setHeader("refresh_token",
//                            mapResult.get("refresh_token"));

                            //把新获取到的refresh_token存到redis
                            userInfoService.setRefreshToken(userInfoVO.getId(),mapResult.get("refresh_token"));
                            response.setHeader("isRefreshToken", "yes");
                            request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
                            return;
                        }
                    } catch (Exception e) {
//                    e.printStackTrace();
                        // 获取刷新令牌失败时（刷新令牌过期时），返回指定格式的错误信息
                        response.setHeader("Content-Type", "application/json;charset=utf-8");
                        response.getWriter().print("{\"code\":403,\"msg\":\"请登录\"}");
                        response.getWriter().flush();
                        return;
                    }
                }else{
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    response.getWriter().print("{\"code\":403,\"msg\":\"请登录\"}");
                    response.getWriter().flush();
                    return;
                }
            }
            super.commence(request,response,authException);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
