package com.love.product.service;

import com.love.product.entity.base.Result;
import com.love.product.entity.vo.HomeVO;
import com.love.product.service.impl.HomeServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @PackageName: com.love.product.service
 * @Description: HomeService
 * @Author: Administrator
 * @Date: 2023/7/21 14:51
 */

public interface HomeService {
    Result<HomeVO> init();

   Result< Map<String,Integer>> lineCount();

    Result hot(String type);
}
