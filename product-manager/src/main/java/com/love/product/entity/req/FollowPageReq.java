package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @date 2022-12-28 9:23
 * @describe 关注请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowPageReq extends PageQuery {

    @ApiModelProperty(value = "查询类型 1我的关注 2我的粉丝")
    private Integer followType;
}
