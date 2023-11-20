package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: UserPageReq
 * @Author: Administrator
 * @Date: 2023/8/8 9:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageReq extends PageQuery {

    @ApiModelProperty(value = "用户昵称或邮箱")
    public String username;

    @ApiModelProperty(value = "身份")
    public Integer roleId;
}
