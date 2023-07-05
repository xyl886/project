package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @PackageName: com.love.product.entity
 * @Description: Message
 * @Author: Administrator
 * @Date: 2023/7/4 11:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value ="message")
public class Message extends BaseEntity {

    @ApiModelProperty(value = "发送者id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromId;

    @ApiModelProperty(value = "接收者id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toId;

    @ApiModelProperty(value = "消息内容")
    private String message;

}
