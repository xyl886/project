package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author hjf
 * @date 2022-10-29 14:51
 * @describe 帖子
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="posts")
public class Posts extends BaseEntity {

    @ApiModelProperty(value = "帖子所属用户主键")
    public Long userId;

    /**
     * {@link com.love.product.enumerate.PostsType}
     */
    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    public Integer postsType;

    @ApiModelProperty(value = "标题")
    public String title;

    @ApiModelProperty(value = "内容")
    public String content;

    /**
     * {@link com.love.product.enumerate.School}
     */
    @ApiModelProperty(value = "校区 1官塘校区 2社湾校区")
    public Integer school;

    @ApiModelProperty(value = "单价")
    public BigDecimal price;

    @ApiModelProperty(value = "封面图片")
    public String coverPath;

    @ApiModelProperty(value = "图片，多张英文逗号分割")
    public String imgPath;

    @ApiModelProperty(value = "浏览数量")
    public int browseNum;

    @ApiModelProperty(value = "收藏数量")
    public int collectNum;

    @ApiModelProperty(value = "点赞数量")
    public int likeNum;

    @ApiModelProperty(value = "评论数量")
    public int commentNum;

    @ApiModelProperty(value = "版本号")
    public int version;

    /**
     * {@link com.love.product.enumerate.YesOrNo}
     */
    @ApiModelProperty(value = "状态")
    public Integer status;
}
