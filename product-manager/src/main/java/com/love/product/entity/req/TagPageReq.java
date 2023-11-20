package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: TagPageReq
 * @Author: Administrator
 * @Date: 2023/8/1 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagPageReq extends PageQuery {

    public String tagName;
}
