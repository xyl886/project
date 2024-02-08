package com.love.product.interceptor;

import com.google.common.collect.ImmutableList;
import com.love.product.annotation.AccessLimit;
import com.love.product.entity.base.Result;
import com.love.product.service.RedisService;
import com.love.product.util.IpUtil;
import com.love.product.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class AccessLimitAspect {
    @Resource
    private RedisService redisService;

    @Pointcut("@annotation(com.love.product.annotation.AccessLimit)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AccessLimit annotation = method.getAnnotation(AccessLimit.class);
        //注解名称
        String name = annotation.name();
        //注解key
        String key = annotation.key();
        //访问IP
        String ip = IpUtil.getIpAddress(request);
        //过期时间
        int limitPeriod = annotation.period();
        //过期次数
        int limitCount = annotation.count();

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix() + "_", key + "_", ip));
        String source = "limit.lua";
        Long count = redisService.executeLuaScript(source, keys, limitCount, limitPeriod);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, keys, name);
        if (count != null && count <= limitCount) {
            return point.proceed();
        } else {
            return Result.failMsg("操作频繁！");
        }
    }
}
