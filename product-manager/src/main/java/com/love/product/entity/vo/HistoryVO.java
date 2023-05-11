package com.love.product.entity.vo;

import com.love.product.entity.History;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: 浏览记录
 * @Author: Administrator
 * @Date: 2023/5/10 19:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVO extends History {

    @ApiModelProperty(value = "帖子")
    private PostsVO posts;
}
