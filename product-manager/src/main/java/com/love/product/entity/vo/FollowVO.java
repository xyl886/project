package com.love.product.entity.vo;

import com.love.product.entity.Follow;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-12-28 10:44
 * @describe 关注
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowVO extends Follow {

    @ApiModelProperty(value = "用户")
    private UserBasicInfoVO userInfo;

    @ApiModelProperty(value = "1 我关注了Ta 2Ta关注了我 3互关")
    private Integer followStatus;
}
