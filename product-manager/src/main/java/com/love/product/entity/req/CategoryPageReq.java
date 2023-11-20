package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: CategoryPageReq
 * @Author: Administrator
 * @Date: 2023/7/27 9:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageReq extends PageQuery {

    private String categoryName;

}
