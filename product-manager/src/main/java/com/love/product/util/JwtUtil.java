package com.love.product.util;

import cn.hutool.extra.spring.SpringUtil;
import com.love.product.config.security.JwtToken;
import com.love.product.config.security.TokenConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-11-03 16:51
 * @describe
 */

public class JwtUtil {

    private static TokenConfig getTokenConfig(){
        return SpringUtil.getBean("tokenConfig", TokenConfig.class);
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId(){
        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String localUser = JwtToken.parseToken(request.getHeader("Authorization"), getTokenConfig().getSigningKey());
            if(localUser != null){
                Map<String, Object> userMap = (Map<String, Object>) JsonUtil.json2Map(localUser);
                if(userMap != null){
                    return (Long)userMap.get("userId");
                }
            }
        }
        return null;
    }

}
