package com.love.product.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.dto
 * @Description: CategoryPostCountDTO
 * @Author: Administrator
 * @Date: 2023/7/24 9:18
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPostCountDTO {

    private String Name;

    private Integer Count;

}
