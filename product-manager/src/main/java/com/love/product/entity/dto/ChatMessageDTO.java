package com.love.product.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @PackageName: com.love.product.entity.dto
 * @Description: ChatMessageDTO
 * @Author: Administrator
 * @Date: 2023/7/11 15:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long toId;

    @ApiModelProperty(value = "头像")
    public String fromIdAvatar;

    private String message;

    private LocalDateTime sentTime;

    private boolean isSentByMe;

}
