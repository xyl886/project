package com.love.product.controller;

import com.love.product.entity.Banner;
import com.love.product.entity.base.Result;
import com.love.product.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 轮播图controller
 */
@Api(tags = "轮播图")
@Slf4j
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @ApiOperation("列表")
    @GetMapping("/listAll")
    public Result<List<Banner>> listAll() {
        return bannerService.listAll();
    }

    @ApiOperation("修改")
    @GetMapping("/update")
    public Result<List<Banner>> updateBanner() {
        return bannerService.updateBanner();
    }


}
