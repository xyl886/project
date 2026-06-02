package com.love.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.Notification;
import com.love.product.entity.base.Result;
import com.love.product.mapper.NotificationMapper;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "通知")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private NotificationMapper notificationMapper;

    @ApiOperation("获取通知列表（支持按类型筛选）")
    @GetMapping("/list")
    public Result<List<Notification>> list(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "20") Integer size,
                                           @RequestParam(required = false) Integer type) {
        Long userId = JwtUtil.getUserId();
        if (userId == null) return Result.OK(new ArrayList<>());
        LambdaQueryWrapper<Notification> qw = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getUserId, userId)
                .eq(type != null && type > 0, Notification::getType, type)
                .orderByDesc(Notification::getCreateTime);
        Page<Notification> p = notificationMapper.selectPage(new Page<>(page, size), qw);
        return Result.OK(p.getRecords());
    }

    @ApiOperation("获取未读数（按类型）")
    @GetMapping("/unreadCount")
    public Result<Long> unreadCount(@RequestParam(required = false) Integer type) {
        Long userId = JwtUtil.getUserId();
        if (userId == null) return Result.OK(0L);
        LambdaQueryWrapper<Notification> qw = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0);
        if (type != null && type > 0) qw.eq(Notification::getType, type);
        return Result.OK(notificationMapper.selectCount(qw));
    }

    @ApiOperation("标记已读")
    @PostMapping("/read")
    public Result<?> markRead(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Result.OK();
        notificationMapper.update(null,
                new UpdateWrapper<Notification>().set("is_read", 1).in("id", ids));
        return Result.OK();
    }
}
