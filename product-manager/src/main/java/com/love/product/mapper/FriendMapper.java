package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.Friend;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.entity.req.ReportPageReq;

/**
 * @PackageName: com.love.product.mapper
 * @Description: FriendMapper
 * @Author: Administrator
 * @Date: 2023/8/15 14:39
 */

public interface FriendMapper extends BaseMapper<Friend> {
    void add(Friend follow);

    Page<Friend> selectFriendList(Page<Object> objectPage, ReportPageReq reportPageReq);
}
