package com.love.product.controller.system;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/article")
@RequiredArgsConstructor
@Api(tags = "后台文章管理")
public class SysPostsController {

//    private final PostsService articleService;
//
//    @PostMapping(value = "/list")
//    @ApiOperation(value = "文章列表", httpMethod = "POST", response = Result.class, notes = "文章列表")
//    public Result list(@RequestBody Map<String,Object> map) {
//        return articleService.listArticle(map);
//    }
//
//    @GetMapping(value = "/info")
//    @ApiOperation(value = "文章详情", httpMethod = "GET", response = Result.class, notes = "文章详情")
//    public Result getArticleById(Long id) {
//        return articleService.getArticleById(id);
//    }
//
//    @PostMapping(value = "/add")
//    @ApiOperation(value = "保存文章", httpMethod = "POST", response = Result.class, notes = "保存文章")
//    public Result insert(@RequestBody PostsDTO article) {
//        return  articleService.insertArticle(article);
//    }
//
//    @PostMapping(value = "/update")
//    @ApiOperation(value = "修改文章", httpMethod = "POST", response = Result.class, notes = "修改文章")
//    public Result update(@RequestBody PostsDTO article) {
//        return articleService.updateArticle(article);
//    }
//
//    @DeleteMapping(value = "/delete")
//    @ApiOperation(value = "删除文章", httpMethod = "DELETE", response = Result.class, notes = "删除文章")
//    public Result delete(Long id) {
//        return articleService.deleteArticle(id);
//    }
//
//    @DeleteMapping(value = "/deleteBatch")
//    @ApiOperation(value = "批量删除文章", httpMethod = "DELETE", response = Result.class, notes = "批量删除文章")
//    public Result deleteBatch(@RequestBody List<Long> ids) {
//        return articleService.deleteBatchArticle(ids);
//    }
//
//    @PostMapping(value = "/top")
//    @ApiOperation(value = "置顶文章", httpMethod = "POST", response = Result.class, notes = "置顶文章")
//    public Result putTopArticle(@RequestBody PostsDTO article) {
//        return articleService.putTopArticle(article);
//    }
//
//    @PostMapping(value = "/pubOrShelf")
//    @ApiOperation(value = "发布或下架文章", httpMethod = "POST", response = Result.class, notes = "发布或下架文章")
//    public Result publishAndShelf(@RequestBody PostsDTO article) {
//        return articleService.publishAndShelf(article);
//    }
//
//    @GetMapping(value = "/randomImg")
//    @ApiOperation(value = "随机获取一张图片", httpMethod = "GET", response = Result.class, notes = "随机获取一张图片")
//    public Result randomImg() {
//        return articleService.randomImg();
//    }

}
