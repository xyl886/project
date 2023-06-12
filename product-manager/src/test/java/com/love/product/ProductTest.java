package com.love.product;

import com.love.product.enumerate.CodeType;
import com.love.product.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
    private HistoryService historyService;
  @Test
  void Test1(){
//      Long[] ids={1659951556545490946L,1666964117371523073L,1666735062084874241L};
//      System.out.println(historyService.del(1611899504066359297L, ids));
      System.out.println(CodeType.getType("login"));
  }
}
