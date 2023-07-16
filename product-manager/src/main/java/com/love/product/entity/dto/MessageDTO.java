package com.love.product.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @PackageName: com.love.product.entity.dto
 * @Description: MessageDTO
 * @Author: Administrator
 * @Date: 2023/7/11 15:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Long id;

    private Long fromId;

    @ApiModelProperty(value = "头像")
    public String fromIdAvatar;

    private Long toId;

    private String message;

    private LocalDateTime sentTime;

    private boolean isSentByMe;

}
