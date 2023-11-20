package com.love.product.entity.vo;

import com.love.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: CategoryVO
 * @Author: Administrator
 * @Date: 2023/7/26 9:32
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO extends Category {

    private Integer postCount;

}
