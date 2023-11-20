package com.love.product.controller;

import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.service.HistoryService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @PackageName: com.love.product.controller
 * @Description: HistoryController
 * @Author: Administrator
 * @Date: 2023/4/24 9:26
 */
@Slf4j
@Api(tags ="浏览记录")
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<HistoryVO> getPage(@RequestBody HistoryPageReq pageQuery) {
        return historyService.getPage(JwtUtil.getUserId(),pageQuery);
    }
    @ApiOperation("清空")
    @DeleteMapping("/del")
    public Result<?> del(@RequestParam("userId") Long userId,@RequestParam("ids")Long[] ids) {
        log.info(Arrays.toString(ids));
        return historyService.del(userId, ids);
    }
}
