package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Friend;

/**
 * @PackageName: com.love.product.mapper
 * @Description: FriendMapper
 * @Author: Administrator
 * @Date: 2023/8/15 14:39
 */

public interface FriendMapper extends BaseMapper<Friend> {
    void add(Friend follow);
}
