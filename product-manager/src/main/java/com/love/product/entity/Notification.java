package com.love.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.love.product.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("s_notification")
public class Notification extends BaseEntity {

    @ApiModelProperty("接收通知的用户ID")
    private Long userId;

    @ApiModelProperty("触发通知的用户ID")
    private Long fromUserId;

    @ApiModelProperty("通知类型 1点赞 2评论 3回复 4关注 5系统")
    private Integer type;

    @ApiModelProperty("通知内容摘要")
    private String content;

    @ApiModelProperty("关联目标ID")
    private Long targetId;

    @ApiModelProperty("目标类型 1帖子 2评论")
    private Integer targetType;

    @ApiModelProperty("是否已读 0未读 1已读")
    private Integer isRead;
}
