package com.love.product.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: RegisterVO
 * @Author: Administrator
 * @Date: 2023/6/13 14:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO {

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    public String email;

    @ApiModelProperty(value = "验证码")
    public String emailCode;

    @ApiModelProperty(value = "密码")
    public String password;

    @ApiModelProperty(value = "确认密码")
    public String confirmPassword;

}
