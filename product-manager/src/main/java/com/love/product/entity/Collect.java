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
 * @date 2022-12-27 16:18
 * @describe 用户收藏
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="collect")
public class Collect extends BaseEntity {

    @ApiModelProperty(value = "用户主键")
    public Long userId;

    @ApiModelProperty(value = "帖子主键")
    public Long postsId;

    @ApiModelProperty(value = "帖子所属用户主键")
    public Long postsUserId;

    /**
     * {@link com.love.product.enumerate.YesOrNo}
     */
    @ApiModelProperty(value = "消息已读状态")
    public Integer status;
}
