package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.Role;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface RoleService extends IService<Role> {


    Result listRole(String name);

     Result insertRole(Role role);

    Result updateRole(Role role);

    Result deleteBatch(List<Integer> ids);

    Result getCurrentUserRole();

    Result selectById(Integer roleId);

}
