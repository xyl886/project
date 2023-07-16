package com.love.product.entity.vo;

import com.love.product.entity.PostsComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-12-28 10:44
 * @describe 帖子评论
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsCommentVO extends PostsComment {

    @ApiModelProperty(value = "所属用户")
    private UserBasicInfoVO userInfo;

    @ApiModelProperty(value = "是否点赞")
    private Boolean like;
}
