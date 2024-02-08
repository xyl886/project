package com.love.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: LoginVO
 * @Author: Administrator
 * @Date: 2023/6/13 15:14
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    @NotBlank(message = "邮箱不能为空")
    public String email;

    public String emailCode;

    public String password;
    @NotBlank(message = "图形验证码不能为空")
    public String verCode;
    @NotBlank(message = "图形验证码错误")
    public String verKey;

}
