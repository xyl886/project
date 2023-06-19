package com.love.product.controller;

import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.PostsSearchDTO;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.ConditionVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.entity.vo.PostsVO;
import com.love.product.service.PostsService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postsType", value = "发布类型", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "school", value = "校区", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "files", value = "上传图片列表", required = false, dataType = "MultipartFile[]", paramType = "query")
    })
    public Result<Posts> add(PostsVO postsVO){
        return postsService.add(JwtUtil.getUserId(), postsVO);
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<PostsDetailVO> getPage(@RequestBody PostsPageReq postsPageReq) {
        return postsService.getPage(JwtUtil.getUserId(),postsPageReq);
    }

    @ApiOperation("详情")
    @GetMapping("/getDetail")
    @ApiImplicitParam(name = "id", value = "帖子主键", required = true, dataType = "String", paramType = "query")
    public Result<PostsDetailVO> getDetail(@RequestParam("id") Long id) {
        return postsService.getDetail(JwtUtil.getUserId(),id);
    }

    @ApiOperation("浏览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "帖子主键", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/browse")
    public Result<?> browse(@RequestParam("userId") Long userId,@RequestParam("id") Long id) {
        return postsService.browse(userId,id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新帖子信息", notes = "更新帖子信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户主键", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "school", value = "校区", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "Files", value = "新上传图片列表", required = false, dataType = "MultipartFile[]", paramType = "query"),
            @ApiImplicitParam(name = "removeFiles", value = "移除的图片列表", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oldFiles", value = "图片列表", required = false, dataType = "String", paramType = "query")
    })
    public Result<?> update(PostsVO postsVO) {
          if(Objects.equals(JwtUtil.getUserId(), postsVO.getUserId())){
              log.info(String.valueOf(postsVO));
              return postsService.update(postsVO);
          }else {
              return Result.fail("您无权操作!");
          }
    }
    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<PostsSearchDTO>> listPostsBySearch(ConditionVO condition) {
        return Result.OK(postsService.listPostsBySearch(condition));
    }
    @ApiOperation("删除")
    @DeleteMapping ("/del")
    public Result<?> del(Long userId,Long id) {
        return postsService.del(userId, id);
    }
}
