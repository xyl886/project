package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.UserPageReq;
import com.love.product.entity.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
public interface UserInfoService extends IService<UserInfo> {

    UserVO login(LoginVO loginVO);

    UserInfoVO getByEmail(String email);

    UserInfoVO getUserInfoById(Long id);

    UserInfoVO userRegister(RegisterVO registerVO);

    Map<Long, UserInfoVO> listByIds(List<Long> ids);

    UserInfoVO update(Long userId, String nickname, MultipartFile file, Integer gender, String hobby, String remark);

    UserInfoVO getUserInfoAndFansById(Long id);

    void updatePwd(Long id, String currentPassword, String newPassword, String confirmPassword);

    void sendCode(String email, String type);

    void reset(Long userId, RegisterVO resetVO);

    ResultPage<UserVO> listUser(UserPageReq userPageReq);

    UserInfoVO getUserById(Integer id);

    void updateUser(UserInfo user);

    void deleteBatch(List<Integer> ids);

    UserInfoVO getCurrentUserInfo();

    void updatePassword(Map<String, String> map);

    UserInfoVO getUserProfile(Long targetId, Long currentUserId);

    List<Map<String, Object>> searchUser(String keyword);
}
