package com.love.product.controller.system;


import com.love.product.entity.base.Result;
import com.love.product.entity.dto.PostsDTO;
import com.love.product.service.PostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/posts")
@RequiredArgsConstructor
@Api(tags = "后台文章管理")
public class SysPostsController {

    private final PostsService postsService;

    @PostMapping(value = "/audit")
    @ApiOperation(value = "发布或下架文章", httpMethod = "POST", response = Result.class, notes = "发布或下架文章")
    public Result<?> publishAndShelf(@RequestBody PostsDTO postsDTO) {
        postsService.audit(postsDTO);
        return Result.OK();
    }

}
