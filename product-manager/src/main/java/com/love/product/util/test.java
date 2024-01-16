package com.love.product.util;

import com.love.product.service.HomeService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j

public class test {
    @Resource
    private static HomeService homeService;
    public static void main(String[] args) {
        log.info("小懒懒真是大傻逼");
        homeService.lineCount();
    }
}

