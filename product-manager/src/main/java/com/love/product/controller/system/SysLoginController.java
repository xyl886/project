package com.love.product.controller.system;

import com.love.product.entity.base.Result;
import com.love.product.entity.dto.LoginDTO;
import com.love.product.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysLoginController
 * @Author: Administrator
 * @Date: 2023/7/17 14:43
 */
@RestController
@RequestMapping("/system")
@Api(tags = "登录-接口")
public class SysLoginController {
    @Resource
    private LoginService loginService;
    @ApiOperation(value = "账密登录", notes = "账密登录")
    @GetMapping("/login")
    public Result login(
            @Validated LoginDTO  loginDTO,
            HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return loginService.login(loginDTO);
    }
    @GetMapping("logout")
    public Result logout() {

        return Result.OK("退出成功");
    }
}
