package com.love.product.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: UserVO
 * @Author: Administrator
 * @Date: 2023/8/8 9:56
 */
@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "id", value = "表主键")
    public Long id;

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "邮箱")
    public String email;

    @ApiModelProperty(value = "性别")
    public Integer gender;

    @ApiModelProperty(value = "性别名称")
    public String genderText;

    @ApiModelProperty(value = "头像")
    public String avatar;

    @ApiModelProperty(value = "爱好")
    public String hobby;

    @ApiModelProperty(value = "简介")
    public String remark;

    @ApiModelProperty(value = "登录token")
    public String accessToken;

    @ApiModelProperty(value = "关注数量")
    public Integer followNum;

    @ApiModelProperty(value = "粉丝数量")
    public Integer fansNum;

    @ApiModelProperty(value = "发布帖子数")
    public Integer postNum;

    @ApiModelProperty(value = "身份")
    public Integer role;

    @ApiModelProperty(value = "身份")
    public String roleName;

    @ApiModelProperty(value = "状态")
    public Integer status;

    @ApiModelProperty(value = "用户状态")
    private String userStatus;
}
