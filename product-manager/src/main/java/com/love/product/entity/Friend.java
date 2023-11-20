package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @PackageName: com.love.product.entity
 * @Description: Friend
 * @Author: Administrator
 * @Date: 2023/8/15 14:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="s_friend")
public class Friend extends BaseEntity {
    @ApiModelProperty(value = "用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    @ApiModelProperty(value = "被关注用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long friendId;

    /**
     * {@link com.love.product.enumerate.YesOrNo}
     */
    @ApiModelProperty(value = "好友昵称")
    public Integer friendNickname;
    @ApiModelProperty(value = "好友昵称")
    public Integer status;
}
