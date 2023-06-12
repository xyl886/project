package com.love.product.controller;

import com.love.product.annotation.AccessLimit;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Resource
    private RedisService redisService;

    @AccessLimit(seconds = 60,maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    @GetMapping("/code")
    public Result<?> sendCode(@RequestParam("email") String email,
                              @RequestParam("type") String type) {
        return userInfoService.sendCode(email,type);
    }

    @ApiOperation(value = "账密登录", notes = "账密登录")
    @GetMapping("/userLogin")
    public Result<UserInfoVO> login(
            @ApiParam("邮箱") @RequestParam("email") String email,
            @ApiParam("密码") @RequestParam(value = "password",defaultValue = "") String password,
            @ApiParam("验证码") @RequestParam(value = "emailCode",defaultValue = "") String emailCode,
            HttpServletRequest request, HttpServletResponse response
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return userInfoService.login(email,password,emailCode);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @GetMapping("/userRegister")
    public Result<UserInfoVO> userRegister(
            @ApiParam("邮箱") @RequestParam("email") String email,
            @ApiParam("验证码") @RequestParam(value = "emailCode",defaultValue = "") String emailCode,
            @ApiParam("密码") @RequestParam("password") String password,
            @ApiParam("确认密码") @RequestParam("confirmPassword") String confirmPassword
    ) {
        return userInfoService.userRegister(email,emailCode,password,confirmPassword);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @GetMapping("/userLogout")
    public Result<?> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        redisService.del("refresh_token:" + JwtUtil.getUserId());
        redisService.del("user:userinfo:" + JwtUtil.getUserId());
        return Result.OK();
    }
}
