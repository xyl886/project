package com.love.product.controller;

import com.love.product.entity.History;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.entity.vo.PostsVO;
import com.love.product.service.HistoryService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller
 * @Description: HistoryController
 * @Author: Administrator
 * @Date: 2023/4/24 9:26
 */
@Api(tags ="浏览记录")
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<HistoryVO> getPage(@RequestBody PageQuery pageQuery) {
        return historyService.getPage(JwtUtil.getUserId(),pageQuery);
    }

}
