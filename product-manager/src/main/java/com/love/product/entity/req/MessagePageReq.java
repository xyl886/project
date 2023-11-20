package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.req
 * @Description: MessagePageReq
 * @Author: Administrator
 * @Date: 2023/8/7 14:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePageReq extends PageQuery {

    private String content;

}
