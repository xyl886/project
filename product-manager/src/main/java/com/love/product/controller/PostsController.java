package com.love.product.controller;

import com.love.product.annotation.AccessLimit;
import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.ConditionVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.entity.vo.PostsVO;
import com.love.product.service.PostsService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 帖子controller
 */
@Api(tags = "帖子")
@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Resource
    private PostsService postsService;

    @PostMapping("/add")
    @ApiOperation(value = "添加", notes = "添加")
    public Result<Posts> add(PostsVO postsVO,
                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
        if (files != null && files.length > 0) {
            postsVO.setFiles(files);
        }
        return Result.OK("已提交，等待管理员审核！", postsService.add(postsVO));
    }

    @GetMapping("/listHot")
    public Result<List<PostsDetailVO>> listHot() {
        return Result.OK(postsService.listHot());
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    @AccessLimit(prefix = "limit",key = "post_query", name = "帖子查询接口")
    public ResultPage<PostsDetailVO> getPage(@RequestBody PostsPageReq postsPageReq) {
        return postsService.getPage(postsPageReq);
    }

    @ApiOperation("详情")
    @GetMapping("/getDetail")
    @ApiImplicitParam(name = "id", value = "帖子主键", required = true, dataType = "String", paramType = "query")
    public Result<PostsDetailVO> getDetail(@RequestParam("id") Long id) {
        return Result.OK(postsService.getDetail(id));
    }

    @ApiOperation("浏览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "帖子主键", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/browse")
    public Result<?> browse(@RequestParam(value = "userId", required = false) Long userId, @RequestParam("id") Long id) {
        postsService.browse(userId, id);
        return Result.OK();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新帖子信息", notes = "更新帖子信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "Files", value = "新上传图片列表", required = false, dataType = "MultipartFile[]", paramType = "query"),
            @ApiImplicitParam(name = "removeFiles", value = "移除的图片列表", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oldFiles", value = "图片列表", required = false, dataType = "String", paramType = "query")
    })
    public Result<?> update(@Validated PostsVO postsVO) {
        postsService.update(postsVO);
        return Result.OK("修改成功");
    }

    @ApiOperation("获取指定用户的帖子列表")
    @GetMapping("/userPosts")
    public Result<List<PostsDetailVO>> userPosts(@RequestParam Long userId,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "20") Integer size) {
        return Result.OK(postsService.getUserPosts(userId, page, size));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<Posts>> listPostsBySearch(ConditionVO condition) {
        return Result.OK(postsService.listPostsBySearch(condition));
    }

    @ApiOperation("删除")
    @DeleteMapping("/del")
    public Result<?> del(Long userId, Long id) {
        postsService.del(userId, id);
        return Result.OK("删除成功");
    }

    @ApiOperation("彻底删除")
    @DeleteMapping("/delete")
    public Result<?> delete(Long userId, Long id) {
        postsService.delete(userId, id);
        return Result.OK("彻底删除成功");
    }

    @ApiOperation("还原")
    @PostMapping("/restore")
    public Result<?> restore(Long userId, Long id) {
        postsService.restore(userId, id);
        return Result.OK("成功还原！");
    }
}
