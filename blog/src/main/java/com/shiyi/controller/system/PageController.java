package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.Result;
import com.shiyi.entity.Page;
import com.shiyi.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/system/page")
@Api(tags = "后台页面管理")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "页面列表", httpMethod = "GET", response = Result.class, notes = "页面列表")
    public Result list() {
        return pageService.listPage();
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/page/add")
    @ApiOperation(value = "新增页面", httpMethod = "POST", response = Result.class, notes = "新增页面")
    @OperationLogger(value = "新增页面")
    public Result insert(@RequestBody Page page) {
        return pageService.insertPage(page);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/page/update")
    @ApiOperation(value = "修改页面", httpMethod = "POST", response = Result.class, notes = "修改页面")
    @OperationLogger(value = "修改页面")
    public Result update(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/page/delete")
    @ApiOperation(value = "删除页面", httpMethod = "DELETE", response = Result.class, notes = "删除页面")
    @OperationLogger(value = "删除页面")
    public Result deletePageById(Long id) {
        return pageService.deletePageById(id);
    }
}

