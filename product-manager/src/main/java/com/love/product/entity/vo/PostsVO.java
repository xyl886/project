package com.love.product.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.love.product.entity.Posts;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2022-12-28 9:23
 * @describe 帖子请求体
 */
@EqualsAndHashCode()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsVO{

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "id", value = "表主键")
    public Long id;

    @ApiModelProperty(value = "帖子所属用户主键")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long userId;

    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    public Integer postsType;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题")
    public String title;

    @NotBlank(message = "帖子描述不能为空")
    @ApiModelProperty(value = "帖子描述")
    public String description;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    public String content;

    @NotNull(message = "请选择分类")
    @ApiModelProperty(value = "校区")
    public Integer school;

    @ApiModelProperty(value = "单价")
    public BigDecimal price;

    @ApiModelProperty(value = "新上传文件")
    private MultipartFile[] Files;

    @ApiModelProperty(value = "已上传文件")
    private String oldFiles;

    @ApiModelProperty(value = "移除的文件")
    private String removeFiles;
}
