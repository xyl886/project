package com.love.product.controller;

import com.love.product.entity.Posts;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.req.PostsReq;
import com.love.product.entity.vo.PostsVO;
import com.love.product.service.PostsService;
import com.love.product.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 帖子controller
 */
@Api(tags = "帖子")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
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
            @ApiImplicitParam(name = "price", value = "校区", required = false, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "files", value = "上传图片列表", required = false, dataType = "MultipartFile[]", paramType = "query")
    })
    public Result<Posts> add(
            @RequestParam("postsType")  Integer postsType,
            @RequestParam("title")  String title,
            @RequestParam("content")  String content,
            @RequestParam("school")  Integer school,
            @RequestParam(value = "price",required = false) BigDecimal price,
            @RequestParam(value = "files",required = false) MultipartFile[] files
            ) throws IOException {
        PostsReq postsReq = new PostsReq();
        postsReq.setPostsType(postsType);
        postsReq.setTitle(title);
        postsReq.setContent(content);
        postsReq.setSchool(school);
        postsReq.setPrice(price);
        postsReq.setFiles(files);

//        System.out.println(JwtUtil.getUserId()+"  "+postsReq);
        return postsService.add(JwtUtil.getUserId(),postsReq);
    }

    @ApiOperation("分页")
    @PostMapping("/getPage")
    public ResultPage<PostsVO> getPage(@RequestBody PostsPageReq postsPageReq) {
        return postsService.getPage(JwtUtil.getUserId(),postsPageReq);
    }

    @ApiOperation("详情")
    @GetMapping("/getDetail")
    public Result<PostsVO> getDetail(@RequestParam("id") Long id) {
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
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "school", value = "校区", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "files", value = "上传图片列表", required = false, dataType = "MultipartFile[]", paramType = "query")
    })
    public Result<?> update(
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("school") String school,
            @RequestParam("content") String content,
            @RequestParam(value = "files",required = false) MultipartFile[] files
    ) {
        {
            PostsReq postsReq = new PostsReq();
            postsReq.setId(id);
            postsReq.setTitle(title);
            postsReq.setContent(content);
            postsReq.setSchool(Integer.valueOf(school));
            postsReq.setFiles(files);

            log.info("当前用户id为:"+JwtUtil.getUserId()+";帖子修改为:"+postsReq+","+files.length+","+id+","+title+","+content+","+school);
            return postsService.update(id,postsReq,title,content,school);
        }
    }

    @ApiOperation("删除")
    @DeleteMapping ("/del")
    public Result<?> del(Long userId,Long id) {
        return postsService.del(userId, id);
    }
}
