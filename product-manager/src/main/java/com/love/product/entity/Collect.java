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
 * @describe 用户收藏
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="s_collect")
public class Collect extends BaseEntity {

    @ApiModelProperty(value = "用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    @ApiModelProperty(value = "帖子主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long postsId;

    @ApiModelProperty(value = "帖子所属用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long postsUserId;

    /**
     * {@link com.love.product.enums.YesOrNo}
     */
    @ApiModelProperty(value = "消息已读状态")
    public Integer status;
}
