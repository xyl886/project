package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Administrator
 * @date 2022-10-29 14:51
 * @describe 首页轮播图
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "banner")
public class Banner extends BaseEntity {

    @ApiModelProperty(value = "图片路径")
    public String imgPath;

    @ApiModelProperty(value = "排序")
    public Integer sort;

    @ApiModelProperty(value = "备注")
    public String remark;

    /**
     * {@link com.love.product.enumerate.YesOrNo}
     */
    @ApiModelProperty(value = "状态")
    public Integer status;
}
