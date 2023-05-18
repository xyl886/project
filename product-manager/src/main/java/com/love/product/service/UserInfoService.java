package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户登录
     */
    Result<UserInfoVO> login(String phone, String password);

    UserInfoVO getByPhone(String phone);

    /**
     * 用户详情
     */
    UserInfoVO getUserInfoById(Long id);

    void setRefreshToken(Long userId, String refreshToken);

    /**
     * 用户注册
     */
    Result<UserInfoVO> userRegister(String userName, String password, String confirmPassword);

    /**
     * 用户列表
     */
    Map<Long, UserInfoVO> listByIds(List<Long> ids);

    Result<UserInfoVO> update(Long userId, String nickname, MultipartFile file,Integer  gender,String hobby,String remark);

    UserInfoVO getUserInfoAndFansById(Long id);

    Result<UserInfoVO> update(String phone, String password);
}
