package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Tags;
import com.love.product.entity.UserAuth;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.RolePageReq;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: RoleService
 * @Author: Administrator
 * @Date: 2023/8/8 14:23
 */

public interface RoleService extends IService<UserAuth> {
    ResultPage<UserAuth> listUser(RolePageReq pageQuery);

    Result insertRole(UserAuth userAuth);

    Result updateRole(UserAuth userAuth);

    Result deleteById(Integer id);

    Result deleteBatch(List<Integer> ids);
}
