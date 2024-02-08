package com.love.product.config.security;

import com.alibaba.fastjson2.JSON;
import com.love.product.config.exception.BizException;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.love.product.constant.RedisKeyConstant.USER_USERINFO;

/**
 * @author Administrator
 * @date 2022-10-31 15:35
 * @describe OAuth2配置类
 * <p>
 * 1.配置令牌加载的属性
 * 2.自定义用户信息到token令牌内
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private TokenConfig tokenConfig;

    /**
     * 注入authenticationManager
     * 来支持 password grant type
     */
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 注入userDetailService
     * 来支持 refresh_token grant type
     * 人话讲 就是toekn失效 需要用到refresh_token去重新请求 /oauth/token来签发新的token和refresh_token
     */
    @Resource
    private UserDetailsService userDetailService;
    @Resource
    private RedisService redisService;
    @Resource
    private UserInfoService userInfoService;

    /**
     * 定义oauth/token类接口信息
     *
     * @description tokenConfig map
     * map.get("clientId")    类比为token的用户名
     * map.get("secret")    类比为token的密码
     * map.get("grantTypes")表示授权类型 grant_type： password(密码模式)
     * map.get("scopes")权限范围
     * map.get("accessTokenValidity")token有效期
     * map.get("refreshTokenValidity")刷新token有效时间
     * map.get("resourceId")定义资源令牌头部,资源服务器验证令牌时用到
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        Map<String, String> map = tokenConfig.getConfig();
        clients.inMemory()
                .withClient(map.get("clientId"))
                .secret("{noop}" + map.get("secret"))
                .authorizedGrantTypes(map.get("grantTypes"), "refresh_token")
                .scopes(map.get("scopes"))
                .accessTokenValiditySeconds(Integer.parseInt(map.get("accessTokenValidity")))
                .refreshTokenValiditySeconds(Integer.parseInt(map.get("refreshTokenValidity")))
                .resourceIds(tokenConfig.getResourceId())
//                .authorities("ADMIN")
//                .redirectUris("http://localhost:8882/login") // 认证成功重定向URL
                .autoApprove(true);// 自动认证

    }

    /**
     * token令牌配置
     *
     * @description 1.定义自定义token生成方式、tokenStore、、认证管理器
     * 2.定义token加解密转换器
     * 3.定义token请求方式
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(userDetailService)//支持refresh_token机制
                .reuseRefreshTokens(tokenConfig.isRefreshToken());//和配置文件对应的 具体看application.yml最后一项
    }

    /**
     * OAuth2服务配置
     *
     * @description 1.允许/oauth/token被调用，默认deny
     * 2.允许所有检查token，默认deny。必须加，否则check_token不能访问显示401未授权错误
     * 3.允许表单认证
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
//            .tokenKeyAccess("permitAll()")
//            .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 生成jwt令牌
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /***
             * 重写增强token方法,用于自定义一些token总需要封装的信息
             * @return
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                Authentication user = authentication.getUserAuthentication();
                String email = user.getName();
                if (email == null) {
                    throw new BizException(403, "请重新登录");
                }
                UserInfoVO userInfoVO = userInfoService.getByEmail(email);
                if (userInfoVO == null) {
                    throw new BizException(403, "请重新登录");
                }
                log.info(String.valueOf(userInfoVO));
                userInfoVO = (UserInfoVO) redisService.get(USER_USERINFO + userInfoVO.getId());
                Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
                // 得到用户名，去处理数据库可以拿到当前用户的信息和角色信息（需要传递到服务中用到的信息）
                final Map<String, Object> additionalInformation = new HashMap<>();
                // Map假装用户实体
                Map<String, Object> userinfo = new HashMap<>();
                userinfo.put("userId", userInfoVO.getId());
                userinfo.put("email", userInfoVO.getEmail());
                userinfo.put("nickname", userInfoVO.getNickname());
                userinfo.put("authOrity", authority);
                additionalInformation.put("userinfo", JSON.toJSONString(userinfo));
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return super.enhance(accessToken, authentication);
            }
        };
        // 生成签名的key,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        accessTokenConverter.setSigningKey(tokenConfig.getSigningKey());
        return accessTokenConverter;
    }


}
