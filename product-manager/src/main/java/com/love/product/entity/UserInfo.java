package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Administrator
 * @date 2022-10-19 10:24
 * @describe 用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="s_user_info")
public class UserInfo extends BaseEntity {

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "邮箱")
    public String email;

    @ApiModelProperty(value = "登录密码 加密")
    public String password;

    @ApiModelProperty(value = "登录密码 明文密码")
    public String originalPassword;

    @ApiModelProperty(value = "头像")
    public String avatar;

    /**
     * {@link com.love.product.enums.Gender}
     */
    @ApiModelProperty(value = "性别")
    public Integer gender;

    /**
     * {@link com.love.product.enums.YesOrNo}
     */
    @ApiModelProperty(value = "状态")
    public Integer status;

    @ApiModelProperty(value = "爱好")
    public String hobby;

    @ApiModelProperty(value = "简介")
    public String remark;

    @ApiModelProperty(value = "身份")
    public Integer role;
}
