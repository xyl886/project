package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @date 2022-10-19 10:24
 * @describe 用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="user_info")
public class UserInfo extends BaseEntity {

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "手机号")
    public String email;

    @ApiModelProperty(value = "登录密码 加密")
    public String password;

    @ApiModelProperty(value = "登录密码 明文密码")
    public String originalPassword;

    @ApiModelProperty(value = "头像")
    public String avatar;

    /**
     * {@link com.love.product.enumerate.Gender}
     */
    @ApiModelProperty(value = "性别")
    public Integer gender;

    /**
     * {@link com.love.product.enumerate.YesOrNo}
     */
    @ApiModelProperty(value = "状态")
    public Integer status;

    @ApiModelProperty(value = "爱好")
    public String hobby;

    @ApiModelProperty(value = "备注")
    public String remark;

}
