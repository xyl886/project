package com.love.product.entity.vo;

import com.love.product.entity.Report;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: ReportVO
 * @Author: Administrator
 * @Date: 2023/8/12 15:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReportVO extends Report {

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "邮箱")
    public String email;

    @ApiModelProperty(value = "头像")
    public String avatar;

    @ApiModelProperty(value = "标题")
    public String title;
}
