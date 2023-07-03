package com.shiyi.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.BusinessLogger;
import com.shiyi.common.Result;
import com.shiyi.service.UserAuthService;
import com.shiyi.dto.EmailLoginDTO;
import com.shiyi.dto.EmailRegisterDTO;
import com.shiyi.dto.QQLoginDTO;
import com.shiyi.dto.UserAuthDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@Api(tags = "登录接口")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserAuthService userAuthService;

    @RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
    @ApiOperation(value = "邮箱登录", httpMethod = "POST", response = Result.class, notes = "邮箱登录")
    public Result emailLogin(@Valid @RequestBody EmailLoginDTO emailLoginDTO){
        return userAuthService.emailLogin(emailLoginDTO);
    }

    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    @ApiOperation(value = "邮箱账号注册", httpMethod = "POST", response = Result.class, notes = "邮箱账号注册")
    public Result emailRegister(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO){
        return userAuthService.emailRegister(emailRegisterDTO);
    }

    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    @BusinessLogger(value = "个人中心模块-邮箱账号修改密码",type = "修改",desc = "邮箱账号修改密码")
    public Result updatePassword(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO){
        return userAuthService.updatePassword(emailRegisterDTO);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "QQ登录", httpMethod = "POST", response = Result.class, notes = "QQ登录")
    public Result login(@Valid @RequestBody QQLoginDTO qqLoginDTO){
        return userAuthService.qqLogin(qqLoginDTO);
    }

    @RequestMapping(value = "/gitEELogin",method = RequestMethod.GET)
    @ApiOperation(value = "gitEE登录", httpMethod = "GET", response = Result.class, notes = "gitEE登录")
    public Result gitEELogin(String code){
        return userAuthService.giteeLogin(code);
    }

    @RequestMapping(value = "/weiboLogin",method = RequestMethod.GET)
    @ApiOperation(value = "微博登录", httpMethod = "GET", response = Result.class, notes = "微博登录")
    public Result weiboLogin(String code){
        return userAuthService.weiboLogin(code);
    }

    @RequestMapping(value = "/sendEmailCode",method = RequestMethod.GET)
    @ApiOperation(value = "发送邮箱验证码", httpMethod = "GET", response = Result.class, notes = "发送邮箱验证码")
    public Result sendEmailCode(String email){
        return userAuthService.sendEmailCode(email);
    }

    @RequestMapping(value = "/bindEmail",method = RequestMethod.POST)
    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-绑定邮箱",type = "修改",desc = "绑定邮箱")
    public Result bindEmail(@RequestBody UserAuthDTO vo){
        return userAuthService.bindEmail(vo);
    }

    @BusinessLogger(value = "个人中心模块-修改用户信息",type = "修改",desc = "修改用户信息")
    @SaCheckLogin
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public Result updateUser(@RequestBody UserAuthDTO vo){
        return userAuthService.updateUser(vo);
    }
}

