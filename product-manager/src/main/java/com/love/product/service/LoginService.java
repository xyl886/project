package com.love.product.service;

import com.love.product.entity.base.Result;
import com.love.product.entity.dto.LoginDTO;

/**
 * @PackageName: com.love.product.service
 * @Description: LoginService
 * @Author: Administrator
 * @Date: 2023/7/17 14:45
 */

public interface LoginService {
    Result login(LoginDTO vo);
}
