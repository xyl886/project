package com.love.product.mapper;


import com.love.product.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * @PackageName: com.love.product.mapper
 * @Description: SysUserMapper
 * @Author: Administrator
 * @Date: 2023/7/17 14:51
 */

public interface SysUserMapper {
  UserInfoVO selectNameAndPassword(@Param("email") String email);

}
