package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @date 2022-12-30 15:00
 * @describe
 */
@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/update")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "头像", required = false, dataType = "MultipartFile", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "hobby", value = "爱好", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "个人简介", required = true, dataType = "String", paramType = "query")
    })
    public Result<UserInfoVO> update(
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "file",required = false) MultipartFile file,
            @RequestParam("gender") Integer gender,
            @RequestParam("hobby") String hobby,
            @RequestParam("remark") String remark
    ) {
        return userInfoService.update(JwtUtil.getUserId(),nickname,file,gender,hobby,remark);
    }

    @ApiOperation(value = "用户详情", notes = "用户详情")
    @GetMapping("/detail")
    public Result<UserInfoVO> detail() {
        UserInfoVO userInfoVO = userInfoService.getUserInfoAndFansById(JwtUtil.getUserId());
        if(userInfoVO != null){
            return Result.OK(userInfoVO);
        }else{
            return Result.fail();
        }
    }
    @PostMapping ("/updateUserPwd")
    @ApiOperation(value = "更改用户密码", notes = "更改用户密码")
    public Result<?> updateUserPwd(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword
    ) {
        return userInfoService.updatePwd(JwtUtil.getUserId(), currentPassword, newPassword, confirmPassword);
    }
    @PostMapping ("/reset")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public Result<?> updateUserPwd(
            @RequestParam("email") String email,
            @RequestParam("emailCode") String emailCode,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword
    ) {
        return userInfoService.reset(JwtUtil.getUserId(), email, emailCode,password, confirmPassword);
    }
}
