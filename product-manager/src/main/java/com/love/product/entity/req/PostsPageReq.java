package com.love.product.entity.req;

import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @date 2022-12-28 9:23
 * @describe 帖子请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsPageReq extends PageQuery {

    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    private Integer postsType;

    @ApiModelProperty(value = "标签id")
    private Long tagId;

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "状态id")
    public Integer status;
}
