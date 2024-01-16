package com.love.product.controller;

import com.love.product.annotation.AccessLimit;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.*;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.love.product.constant.RedisKeyConstant.REFRESH_TOKEN;
import static com.love.product.constant.RedisKeyConstant.USER_USERINFO;

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

    @ApiOperation(value = "生成验证码拼图")
    @PostMapping("get-captcha")
    public Result getCaptcha(@RequestBody Captcha captcha) {
        return Result.OK(userInfoService.getCaptcha(captcha));
    }

    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    @GetMapping("/code")
    public Result<?> sendCode(@RequestParam("email") String email,
                              @RequestParam("type") String type) {
        return userInfoService.sendCode(email, type);
    }

    @ApiOperation(value = "账密登录", notes = "账密登录")
    @GetMapping("/userLogin")
    public Result<UserVO> login(
            @Validated LoginVO loginVO,
            HttpServletRequest request, HttpServletResponse response
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return userInfoService.login(loginVO);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @AccessLimit(seconds = 60, maxCount = 1)
    @GetMapping("/userRegister")
    public Result<UserInfoVO> userRegister(RegisterVO registerVO) {
        return userInfoService.userRegister(registerVO);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @GetMapping("/userLogout")
    public Result<?> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        redisService.del(REFRESH_TOKEN + JwtUtil.getUserId());
        redisService.del(USER_USERINFO + JwtUtil.getUserId());
        return Result.OK();
    }
}
