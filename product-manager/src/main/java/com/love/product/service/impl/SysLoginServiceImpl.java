package com.love.product.service.impl;

import cn.hutool.core.util.IdUtil;
import com.love.product.config.BizException;
import com.love.product.entity.dto.LoginDTO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.entity.vo.ValidateCodeVO;
import com.love.product.enums.Gender;
import com.love.product.mapper.SysUserMapper;
import com.love.product.service.LoginService;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.CommonUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @PackageName: com.love.product.service.impl
 * @Description: SysLoginServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/17 14:47
 */
@Service
@Slf4j
public class SysLoginServiceImpl implements LoginService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserInfoVO login(LoginDTO loginDTO) {
        //校验用户名和密码
        UserInfoVO userInfoVO = sysUserMapper.selectNameAndPassword(loginDTO.getEmail());
        if (userInfoVO == null) {
            throw new BizException("用户不存在");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), userInfoVO.password)) {
            throw new BizException("密码错误");
        }
        // 仅允许管理员登录管理后台
        if (userInfoVO.getRole() == null || userInfoVO.getRole() != 3) {
            throw new BizException("无管理员权限");
        }

        // Sa-Token 登录
        StpUtil.login(userInfoVO.getId());
        //缓存用户信息到Redis
        userInfoVO.setAccessToken(StpUtil.getTokenValue());
        userInfoVO.setEmail(loginDTO.getEmail());
        Gender gender = Gender.valueOf(userInfoVO.getGender());
        userInfoVO.setGenderText(gender.getText());

        return userInfoVO;
    }
}
