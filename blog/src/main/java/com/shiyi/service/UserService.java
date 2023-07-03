package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.User;

import java.util.List;
import java.util.Map;

/**
 *
 * @author blue
 * @date: 2021/7/30 17:17
 */
public interface UserService extends IService<User> {

    Result listUser(String username, Integer loginType);

    Result getUserById(Integer id);

    Result insertUser(User user);

    Result updateUser(User user);

    Result deleteBatch(List<Integer> ids);

    Result getCurrentUserInfo();

    Result getCurrentUserMenu();

    Result updatePassword(Map<String, String> map);

    Result listOnlineUsers(String keywords);

    Result kick(String token);
}
