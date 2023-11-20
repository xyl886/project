package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: RolePageReq
 * @Author: Administrator
 * @Date: 2023/8/8 14:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePageReq extends PageQuery {

    @ApiModelProperty(value = "角色名称")
    public String roleName;
}
