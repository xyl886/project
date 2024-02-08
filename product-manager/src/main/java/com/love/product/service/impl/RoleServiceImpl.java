package com.love.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.exception.BizException;
import com.love.product.entity.UserAuth;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.RolePageReq;
import com.love.product.mapper.RoleMapper;
import com.love.product.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.love.product.entity.base.ResultCode.ROLE_IS_EXIST;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: RoleServiceImpl
 * @Author: Administrator
 * @Date: 2023/8/8 14:23
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, UserAuth> implements RoleService {
    @Override
    public ResultPage<UserAuth> listUser(RolePageReq pageQuery) {
        log.info(String.valueOf(pageQuery));
       LambdaQueryWrapper<UserAuth> queryWrapper= new LambdaQueryWrapper<>();
       queryWrapper.eq(StringUtils.isNotBlank(pageQuery.roleName),UserAuth::getRoleName,pageQuery.roleName)
               .orderByDesc(UserAuth::getUpdateTime);
     Page<UserAuth> page = page(pageQuery.build(),queryWrapper);
        List<UserAuth> list = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = new ArrayList<>(page.getRecords());
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public Result insertRole(UserAuth userAuth) {
        UserAuth vo = baseMapper.selectOne(new QueryWrapper<UserAuth>()
                .eq("role_name", userAuth.roleName));
        if (!ObjectUtil.isNull(vo)) {
            throw new BizException("该角色名称已存在!");
        }
        int rows = baseMapper.insert(userAuth);
        return rows > 0 ? Result.OK("添加成功"): Result.failMsg("添加角色失败");
    }

    @Override
    public Result updateRole(UserAuth userAuth) {
        UserAuth vo = baseMapper.selectOne(new QueryWrapper<UserAuth>()
                .eq("role_name", userAuth.roleName));
        assert vo == null || vo.getId().equals(userAuth.getId()) : ROLE_IS_EXIST.getMsg();
        int rows = baseMapper.updateById(userAuth);
        return rows > 0 ? Result.OK(): Result.failMsg("修改角色失败");
    }

    @Override
    public Result deleteById(Integer id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? Result.OK(): Result.failMsg("删除角色失败");
    }

    @Override
    public Result deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.OK(): Result.failMsg("批量删除角色失败");
    }
}
