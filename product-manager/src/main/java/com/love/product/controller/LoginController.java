package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import com.love.product.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户controller
 */
@Api(tags = "权限控制")
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "账密登录", notes = "账密登录")
    @GetMapping("/userLogin")
    public Result<UserInfoVO> login(
            @ApiParam("电话") @RequestParam("phone") String phone,
            @ApiParam("密码") @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {   //清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return userInfoService.login(phone,password);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @GetMapping("/userRegister")
    public Result<UserInfoVO> userRegister(
            @ApiParam("电话") @RequestParam("phone") String phone,
            @ApiParam("密码") @RequestParam("password") String password,
            @ApiParam("确认密码") @RequestParam("confirmPassword") String confirmPassword
    ) {
        return userInfoService.userRegister(phone,password,confirmPassword);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @GetMapping("/userLogout")
    public Result<?> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        RedisUtil.logout(JwtUtil.getUserId());
        return Result.OK();
    }
}
