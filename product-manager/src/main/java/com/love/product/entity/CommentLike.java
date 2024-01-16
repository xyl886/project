package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @PackageName: com.love.product.entity
 * @Description: CommentLike
 * @Author: Administrator
 * @Date: 2023/7/5 16:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("s_comment_like")
public class CommentLike extends BaseEntity {

    @ApiModelProperty(value = "用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    @ApiModelProperty(value = "帖子主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long commentId;

    /**
     * {@link com.love.product.enums.YesOrNo}
     */
    @ApiModelProperty(value = "消息已读状态")
    public Integer status;

}
