package com.love.product.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.dto
 * @Description: CategoryDTO
 * @Author: Administrator
 * @Date: 2023/8/1 9:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Integer id;

    private String categoryName;

    private String icon;
}
