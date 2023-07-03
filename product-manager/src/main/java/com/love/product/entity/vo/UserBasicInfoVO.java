package com.love.product.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @PackageName: com.love.product.model.VO
 * @Description: UserBasicInfoVO
 * @Author: Administrator
 * @Date: 2023/6/6 17:16
 */
@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoVO {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id", value = "表主键")
    public Long id;

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "性别")
    public Integer gender;

    @ApiModelProperty(value = "性别名称")
    public String genderText;

    @ApiModelProperty(value = "关注数量")
    public Integer followNum;

    @ApiModelProperty(value = "粉丝数量")
    public Integer fansNum;

    @ApiModelProperty(value = "头像")
    public String avatar;

    @ApiModelProperty(value = "简介")
    public String remark;

    @ApiModelProperty(value = "身份")
    public String role;
}
