package com.love.product.util;

import com.love.product.service.PostsLikeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskUtils {
    @Resource
    PostsLikeService postsLikeService;
    // 添加定时任务
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨两点执行
    public void doTask(){
        postsLikeService.transLikeFromRedisToMysql();
        postsLikeService.transLikeCountFromRedisToMysql();
    }
}
