package com.love.product.controller.system;

import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.UserPageReq;
import com.love.product.entity.vo.UserVO;
import com.love.product.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/system/user")
@Api(tags = "系统用户管理-接口")
@RequiredArgsConstructor
public class SysUserController {

    private final UserInfoService userService;

    @PostMapping(value = "/list")
    @ApiOperation(value = "用户列表", httpMethod = "POST", response = Result.class, notes = "用户列表")
    public ResultPage<UserVO> list(@RequestBody UserPageReq userPageReq) {
        return userService.listUser(userPageReq);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "用户详情", httpMethod = "GET", response = Result.class, notes = "用户详情")
    public Result<?> getUserById(Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改用户", httpMethod = "POST", response = Result.class, notes = "修改用户")
    public Result<?> update(@RequestBody UserInfo user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户", httpMethod = "DELETE", response = Result.class, notes = "删除用户")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        return userService.deleteBatch(ids);
    }

    @PostMapping(value = "/getCurrentUserInfo")
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "POST", response = Result.class, notes = "获取当前登录用户信息")
    public Result<?> getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PostMapping(value = "/updatePassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST", response = Result.class, notes = "修改密码")
    public Result<?> updatePassword(@RequestBody Map<String,String> map) {
        return userService.updatePassword(map);
    }

}
