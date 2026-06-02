package com.love.product.service;

import com.love.product.entity.dto.LoginDTO;
import com.love.product.entity.vo.UserInfoVO;

/**
 * @PackageName: com.love.product.service
 * @Description: LoginService
 * @Author: Administrator
 * @Date: 2023/7/17 14:45
 */

public interface LoginService {
    UserInfoVO login(LoginDTO vo);
}
