package com.love.product.annotation;

import java.lang.annotation.*;

/**
 * 接口限流
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 资源名称，用于描述接口功能
     */
    String name() default "";

    /**
     * 资源 key
     */
    String key() default "";

    /**
     * key prefix
     */
    String prefix() default "";

    /**
     * 时间的，单位秒
     * 默认60s过期
     */
    int period() default 60;

    /**
     * 限制访问次数
     * 默认100次
     */
    int count() default 100;
}
