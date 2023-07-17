package com.love.product.controller.system;

import com.love.product.entity.base.Result;
import com.love.product.entity.dto.LoginDTO;
import com.love.product.service.LoginService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysLoginController
 * @Author: Administrator
 * @Date: 2023/7/17 14:43
 */
@RestController
@Api(tags = "登录-接口")
public class SysLoginController {
    @Resource
    private LoginService loginService;
    @PostMapping("login")
    public Result login(@Validated @RequestBody LoginDTO vo) {
        return loginService.login(vo);
    }

    @GetMapping("logout")
    public Result logout() {

        return Result.OK("退出成功");
    }
}
