package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.common.Result;
import com.shiyi.service.impl.HomeServiceImpl;
import com.shiyi.vo.SystemHardwareInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Api(tags = "后台首页")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @SaCheckLogin
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = Result.class, notes = "首页各种统计信息")
    public Result init() {
        return Result.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @SaCheckLogin
    @ApiOperation(value = "首页文章、ip用户、留言统计", httpMethod = "GET", response = Result.class, notes = "首页文章、ip用户、留言统计")
    public Result lineCount() {
        return Result.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
    @SaCheckPermission("/system/home/systemInfo")
    @ApiOperation(value = "服务器监控", httpMethod = "GET", response = Result.class, notes = "服务器监控")
    public Result systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        try {
            systemHardwareInfoVO.copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(systemHardwareInfoVO);
    }

    @GetMapping(value = "/cache")
    @SaCheckPermission("/system/home/cache")
    @ApiOperation(value = "redis监控", httpMethod = "GET", response = Result.class, notes = "redis监控")
    public Result getCacheInfo(){
        return homeService.getCacheInfo();
    }
}
