package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.common.Result;
import com.shiyi.service.LoginService;
import com.shiyi.dto.LoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:37
 */
@RestController
@Api(tags = "登录-接口")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(value = "/captchaImage",method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码", httpMethod = "GET", response = Result.class, notes = "获取验证码")
    public Result getCode(HttpServletResponse response) throws IOException {
        Map<String,String> result = loginService.getCode(response);
        return Result.success("获取验证码成功",result);
    }

    @PostMapping("login")
    public Result login(@Validated @RequestBody LoginDTO vo) {
        return loginService.login(vo);
    }

    @SaCheckLogin
    @GetMapping("logout")
    public Result logout() {
        StpUtil.logout();
        return Result.success("退出成功");
    }
}
