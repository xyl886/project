package com.love.product;

import com.love.product.service.CategoryService;
import com.love.product.service.HomeService;
import com.love.product.service.PostsService;
import com.love.product.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;

import static com.love.product.config.apiCheck.BaiDuAiCheck.discernSensitiveWords;
import static com.love.product.config.apiCheck.BaiDuAiCheck.moderateContent;


/**
 * @PackageName: com.love.product
 * @Description: Test
 * @Author: Administrator
 * @Date: 2023/6/8 14:00
 */
@Slf4j
@SpringBootTest
class ProductTest {
    @Resource
    private HomeService historyService;
    @Resource
    private RedisService redisService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private PostsService postsService;
  @Test
  void Test1() throws UnsupportedEncodingException {
//      Long[] ids={1659951556545490946L,1666964117371523073L,1666735062084874241L};
//      System.out.println(historyService.del(1611899504066359297L, ids));
//      System.out.println(CodeType.getType("login"));

//      PostsPageReq req = new PostsPageReq(1, null, null, "如何写好论文", 3);
//      System.out.println(  postsService.getPageByEs(req));
      // 调用内容审核API
      String text = "如何高效学习？";
      // 处理API响应结果
      System.out.println(moderateContent(text));
      System.out.println(discernSensitiveWords(text,null));
  }
}
