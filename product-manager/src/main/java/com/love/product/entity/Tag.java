package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @PackageName: com.love.product.entity
 * @Description: Tag
 * @Author: Administrator
 * @Date: 2023/5/23 16:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="tag")
public class Tag  extends BaseEntity {

    @ApiModelProperty(value = "标签名")
    public String tagName;
}
