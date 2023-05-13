package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @PackageName: com.love.product.entity
 * @Description: History
 * @Author: Administrator
 * @Date: 2023/4/24 11:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="history")
public class History extends BaseEntity {

    @ApiModelProperty(value = "帖子主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long postsId;

    @ApiModelProperty(value = "浏览用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;
}
