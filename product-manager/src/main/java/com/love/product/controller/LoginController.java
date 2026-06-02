package com.love.product.controller;

import com.love.product.annotation.AccessLimit;
import com.love.product.config.BizException;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.LoginVO;
import com.love.product.entity.vo.RegisterVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.entity.vo.UserVO;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import com.wf.captcha.SpecCaptcha;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.love.product.constant.RedisKeyConstant.REFRESH_TOKEN;
import static com.love.product.constant.RedisKeyConstant.USER_USERINFO;
import static com.love.product.entity.base.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;

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
    @GetMapping("/captcha")
    @AccessLimit(prefix = "limit", key = "captcha", name = "图形验证码接口")
    public Result getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为5分钟
        redisService.set(key, verCode, 5 * 60);
        Map<String, String> captchaMap = new HashMap<>();
        captchaMap.put("key", key);
        captchaMap.put("image", specCaptcha.toBase64());
        return Result.OK(captchaMap);
    }
    private void verifyCaptcha(String verKey, String verCode) {
        // 获取 Redis 中的验证码
        String redisCode = (String) redisService.get(verKey);
        // 判断验证码
        if (verCode == null || redisCode == null || !redisCode.equals(verCode.trim().toLowerCase())) {
            throw new BizException(ERROR_EXCEPTION_MOBILE_CODE);
        }

    }
    @ApiOperation(value = "发送邮箱验证码")
    @AccessLimit(prefix = "limit", key = "code", name = "邮箱验证码接口", period = 60, count = 1)
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    @GetMapping("/code")
    public Result<?> sendCode(@RequestParam("email") String email,
                              @RequestParam("type") String type) {
        return userInfoService.sendCode(email, type);
    }

    @ApiOperation(value = "账密登录", notes = "账密登录")
    @AccessLimit(prefix = "limit", key = "login", name = "登录接口", period = 60, count = 3)
    @PostMapping("/userLogin")
    public Result<UserVO> login(
            @RequestBody @Validated LoginVO loginVO
    ) {
        log.info(String.valueOf(loginVO));
        verifyCaptcha(loginVO.verKey,loginVO.verCode);
        return userInfoService.login(loginVO);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @AccessLimit(prefix = "limit", key = "register", name = "注册接口", period = 60, count = 2)
    @PostMapping("/userRegister")
    public Result<UserInfoVO> userRegister(@RequestBody @Validated RegisterVO registerVO) {
        verifyCaptcha(registerVO.verKey,registerVO.verCode);
        return userInfoService.userRegister(registerVO);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @PostMapping("/userLogout")
    public Result<?> userLogout() {
        redisService.del(REFRESH_TOKEN + JwtUtil.getUserId());
        redisService.del(USER_USERINFO + JwtUtil.getUserId());
        StpUtil.logout();
        return Result.OK();
    }
}
