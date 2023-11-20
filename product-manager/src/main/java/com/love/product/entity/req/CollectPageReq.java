package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: CollectPageReq
 * @Author: Administrator
 * @Date: 2023/8/14 10:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectPageReq extends PageQuery {

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "条件")
    private String condition;

}
