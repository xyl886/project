package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.FriendLink;

import java.util.List;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface FriendLinkService extends IService<FriendLink> {

    Result listFriendLink(String name, Integer status);

    Result insertFriendLink(FriendLink friendLink);

    Result updateFriendLink(FriendLink friendLink);

    Result deleteBatch(List<Integer> ids);

    Result top(Integer id);


    //    ----web端开始-----
    Result webFriendLinkList();

    Result applyFriendLink(FriendLink friendLink);


}
