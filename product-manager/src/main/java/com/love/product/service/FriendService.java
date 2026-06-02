package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Friend;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.FriendPageReq;

/**
 * @PackageName: com.love.product.service
 * @Description: FriendService
 * @Author: Administrator
 * @Date: 2023/8/15 15:46
 */

public interface FriendService extends IService<Friend> {
    void addFriend(Long friendUserId);

    ResultPage getFriendList(Long userId, FriendPageReq friendPageReq);

    void deleteFriend(Long friendUserId);
}
