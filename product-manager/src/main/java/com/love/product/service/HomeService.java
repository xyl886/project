package com.love.product.service;

import com.alibaba.fastjson2.JSONArray;
import com.love.product.entity.vo.HomeVO;

import java.util.Map;

/**
 * @PackageName: com.love.product.service
 * @Description: HomeService
 * @Author: Administrator
 * @Date: 2023/7/21 14:51
 */

public interface HomeService {
    HomeVO init();

    Map<String, Integer> lineCount();

    JSONArray hot(String type);
}
