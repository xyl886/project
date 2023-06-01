package com.love.product.model.VO;

import com.love.product.entity.Posts;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-12-28 10:44
 * @describe 帖子
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsVO extends Posts {

    @ApiModelProperty(value = "校区名称")
    private String schoolName;

    @ApiModelProperty(value = "所属用户")
    private UserInfoVO userInfo;

    @ApiModelProperty(value = "是否收藏")
    private Boolean collect;

    @ApiModelProperty(value = "是否关注")
    private Boolean follow;

    @ApiModelProperty(value = "是否点赞")
    private Boolean like;
}
