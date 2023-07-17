package com.love.product.service.impl;

import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.LoginDTO;
import com.love.product.mapper.SysUserMapper;
import com.love.product.service.LoginService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: SysLoginServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/17 14:47
 */
@Service
public class SysLoginServiceImpl implements LoginService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public Result login(LoginDTO vo) {
        //校验用户名和密码

        UserInfo user = sysUserMapper
                .selectNameAndPassword(
                        vo.getUsername(), new BCryptPasswordEncoder().encode(vo.getPassword()));
        Assert.isTrue(user != null,"密码错误！");

        return Result.OK();
    }
}
