package com.love.product.entity.vo;

import com.love.product.entity.Posts;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xyl
 * @date 2022-12-28 10:44
 * @describe 帖子
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsDetailVO extends Posts {

    @ApiModelProperty(value = "校区名称")
    private String categoryName;

    @ApiModelProperty(value = "标签")
    private List<String> tags;

    @ApiModelProperty(value = "帖子状态")
    private String postStatus;

    @ApiModelProperty(value = "帖子类型")
    private String type;

    @ApiModelProperty(value = "所属用户")
    private UserBasicInfoVO userInfo;

    @ApiModelProperty(value = "是否收藏")
    private Boolean collect;

    @ApiModelProperty(value = "是否关注")
    private Boolean follow;

    @ApiModelProperty(value = "是否点赞")
    private Boolean like;
}
