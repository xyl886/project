package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: Report
 * @Author: Administrator
 * @Date: 2023/8/7 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="s_report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id", value = "表主键")
    public Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "id", value = "表主键")
    private Long reportUserId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "id", value = "表主键")
    private Long postId;

    @ApiModelProperty(name = "id", value = "举报内容")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private LocalDateTime createTime;
}
