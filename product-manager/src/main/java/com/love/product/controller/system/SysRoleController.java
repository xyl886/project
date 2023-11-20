package com.love.product.controller.system;

import com.love.product.entity.UserAuth;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.RolePageReq;
import com.love.product.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysRoleController
 * @Author: Administrator
 * @Date: 2023/8/7 10:55
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理")
public class SysRoleController {
@Resource
private RoleService roleService;
    @PostMapping(value = "/list")
    @ApiOperation(value = "角色列表", httpMethod = "POST", response = Result.class, notes = "用户列表")
    public ResultPage<UserAuth> list(@RequestBody RolePageReq pageQuery) {
        return roleService.listUser(pageQuery);
    }
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "新增角色", httpMethod = "POST", response = Result.class, notes = "新增标签")
    public Result insert(@RequestBody UserAuth userAuth){
        return roleService.insertRole(userAuth);
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改角色", httpMethod = "POST", response = Result.class, notes = "修改标签")
    public Result update(@RequestBody UserAuth userAuth){
        return roleService.updateRole(userAuth);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除角色", httpMethod = "DELETE", response = Result.class, notes = "删除标签")
    public Result deleteById(Integer  id){
        return roleService.deleteById(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除角色", httpMethod = "DELETE", response = Result.class, notes = "批量删除标签")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return roleService.deleteBatch(ids);
    }
}
