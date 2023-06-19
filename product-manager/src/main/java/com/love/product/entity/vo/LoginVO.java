package com.love.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public String email;

    public String emailCode;

    public String password;

}
