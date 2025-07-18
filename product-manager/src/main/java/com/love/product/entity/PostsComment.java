package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Administrator
 * @date 2022-12-27 16:18
 * @describe 帖子评论
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="s_posts_comment")
public class PostsComment extends BaseEntity {

    @ApiModelProperty(value = "上级评论主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long parentId;

    @ApiModelProperty(value = "上级评论用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long parentUserId;

    @ApiModelProperty(value = "用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    @ApiModelProperty(value = "帖子主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long postsId;

    @ApiModelProperty(value = "帖子所属用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long postsUserId;

    @ApiModelProperty(value = "评论内容")
    public String content;

    @ApiModelProperty(value = "点赞数量")
    public Integer likeNum;
    /**
     * {@link com.love.product.enums.YesOrNo}
     */
    @ApiModelProperty(value = "消息已读状态")
    public Integer status;
}
