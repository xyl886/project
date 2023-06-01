package com.love.product.model.VO;

import com.love.product.entity.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-10-31 17:00
 * @describe 用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO extends UserInfo {

    @ApiModelProperty(value = "登录token")
    public String accessToken;

    @ApiModelProperty(value = "性别名称")
    public String genderText;

    @ApiModelProperty(value = "关注数量")
    public Integer followNum;

    @ApiModelProperty(value = "粉丝数量")
    public Integer fansNum;
}
