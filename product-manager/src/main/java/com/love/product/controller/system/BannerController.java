package com.love.product.controller.system;

import com.love.product.entity.Banner;
import com.love.product.entity.Category;
import com.love.product.entity.base.Result;
import com.love.product.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
    @PostMapping("/update")
    public Result<List<Banner>> updateBanner(
            @RequestParam(value = "file",required = false) MultipartFile file,
            @RequestParam(value = "id") Long id) throws IOException {
        log.info(String.valueOf(file));
        return bannerService.updateBanner(file,id);
    }

    @ApiOperation("新增首页轮播图")
    @PostMapping("/add")
    public Result add(MultipartFile file) {
        return bannerService.add(file);
    }
    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除分类", httpMethod = "POST", response = Result.class, notes = "修改文章")
    public Result delete(Long id) {
        return bannerService.deleteBanner(id);
    }
    @RequestMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除分类", httpMethod = "POST", response = Result.class, notes = "批量删除分类")
    public Result deleteBatch(@RequestBody List<Long > ids){
        return bannerService.deleteBatch(ids);
    }

}
