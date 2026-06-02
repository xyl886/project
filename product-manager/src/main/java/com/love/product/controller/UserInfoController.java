package com.love.product.controller;

import com.love.product.entity.base.Result;
import com.love.product.entity.vo.RegisterVO;
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
import java.util.List;
import java.util.Map;

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
        return Result.OK("保存成功", userInfoService.update(JwtUtil.getUserId(), nickname, file, gender, hobby, remark));
    }
    @ApiOperation(value = "用户详情", notes = "用户详情")
    @GetMapping("/detail")
    public Result<UserInfoVO> detail() {
     return Result.OK(userInfoService.getUserInfoAndFansById(JwtUtil.getUserId()));
    }
    @PostMapping ("/updateUserPwd")
    @ApiOperation(value = "更改用户密码", notes = "更改用户密码")
    public Result<?> updateUserPwd(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword
    ) {
        userInfoService.updatePwd(JwtUtil.getUserId(), currentPassword, newPassword, confirmPassword);
        return Result.OKMsg("修改成功！");
    }
    @PostMapping ("/reset")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public Result<?> updateUserPwd(RegisterVO resetVO) {
        userInfoService.reset(JwtUtil.getUserId(), resetVO);
        return Result.OKMsg("重置成功！");
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索用户", notes = "搜索用户")
    public Result<List<Map<String, Object>>> search(@RequestParam String keyword) {
        return Result.OK(userInfoService.searchUser(keyword));
    }

    @GetMapping("/profile")
    @ApiOperation(value = "查看用户主页", notes = "查看用户主页")
    public Result<UserInfoVO> profile(@RequestParam Long id) {
        return Result.OK(userInfoService.getUserProfile(id, JwtUtil.getUserId()));
    }
}
