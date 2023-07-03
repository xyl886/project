package com.love.product.entity.req;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.entity.base.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

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

    /**
     * {@link com.love.product.enumerate.PostsType}
     */
    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    private Integer postsType;

    /**
     * {@link com.love.product.enumerate.School}
     */
    @ApiModelProperty(value = "分类")
    private Integer school;

    @ApiModelProperty(value = "标题")
    private String title;


    @ApiModelProperty(value = "状态")
    public Integer status;
}
