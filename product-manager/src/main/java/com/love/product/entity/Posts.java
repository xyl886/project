package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author hjf
 * @date 2022-10-29 14:51
 * @describe 帖子
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="s_posts_copy")
public class Posts extends BaseEntity {

    @ApiModelProperty(value = "帖子所属用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    /**
     * {@link com.love.product.enums.PostsType}
     */
    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    public Integer postsType;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题")
    public String title;

    @ApiModelProperty(value = "帖子描述")
    public String description;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    public String content;

    @NotNull(message = "请选择分类")
    @ApiModelProperty(value = "校区")
    public Integer school;

    @ApiModelProperty(value = "单价")
    public BigDecimal price;

    @ApiModelProperty(value = "封面图片")
    public String coverPath;

    @ApiModelProperty(value = "图片，多张英文逗号分割")
    public String imgPath;

    @ApiModelProperty(value = "浏览数量")
    public Integer browseNum;

    @ApiModelProperty(value = "收藏数量")
    public Integer collectNum;

    @ApiModelProperty(value = "点赞数量")
    public Integer likeNum;

    @ApiModelProperty(value = "评论数量")
    public Integer commentNum;

    @ApiModelProperty(value = "版本号")
    public Integer version;

    @ApiModelProperty(value = "状态")
    public Integer status;
}
