package com.love.product.mapper;


import com.love.product.entity.UserInfo;

/**
 * @PackageName: com.love.product.mapper
 * @Description: SysUserMapper
 * @Author: Administrator
 * @Date: 2023/7/17 14:51
 */

public interface SysUserMapper {
  UserInfo selectNameAndPassword(String username, String encode) ;

}
