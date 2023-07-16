package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import lombok.*;

/**
 * @PackageName: com.love.product.entity
 * @Description: Category
 * @Author: Administrator
 * @Date: 2023/7/13 14:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="category")
public class Category extends BaseEntity {

    public String icon;

    public String categoryName;

}
