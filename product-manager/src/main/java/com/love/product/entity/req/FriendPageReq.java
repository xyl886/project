package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: FriendPageReq
 * @Author: Administrator
 * @Date: 2023/8/15 14:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendPageReq extends PageQuery {

    @ApiModelProperty(value = "查询类型 1我的关注 2我的粉丝")
    private Integer followType;

}
