package com.love.product.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackageName: com.love.product.entity.dto
 * @Description: PostsDTO
 * @Author: Administrator
 * @Date: 2023/7/17 15:16
 */
@Data
public class PostsDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "id", value = "表主键")
    public Long id;

    @ApiModelProperty(value = "状态")
    public Integer status;

}
