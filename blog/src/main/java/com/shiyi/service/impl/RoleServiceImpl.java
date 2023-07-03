package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.Result;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Role;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.service.RoleService;
import com.shiyi.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    /**
     * 角色列表
     * @param name
     * @return
     */
    @Override
    public Result listRole(String name) {
        Page<Role> data = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), new QueryWrapper<Role>()
                .like(StringUtils.isNotBlank(name),SqlConf.NAME,name));
        return Result.success(data);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertRole(Role role) {
        baseMapper.insert(role);
        baseMapper.insertBatchByRole(role.getMenus(), role.getId());
        return Result.success();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRole(Role role) {
        baseMapper.updateById(role);

        //先删所有权限在新增
        baseMapper.delByRoleId(role.getId(),null);
        baseMapper.insertBatchByRole(role.getMenus(), role.getId());
        return Result.success("修改成功");
    }

    /**
     * 删除角色
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        ids.forEach(id -> baseMapper.delByRoleId(id, null));
        return Result.success();
    }

    /**
     * 获取当前登录用户所拥有的权限
     * @param
     * @return
     */
    @Override
    public Result getCurrentUserRole() {
        Integer roleId = baseMapper.queryByUserId(StpUtil.getLoginId());
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return Result.success(list);
    }

    /**
     * 获取该角色所有的权限
     * @param
     * @return
     */
    @Override
    public Result selectById(Integer roleId) {
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return Result.success(list);
    }
}
