package com.love.product.entity.vo;

import com.love.product.entity.History;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: 浏览记录
 * @Author: Administrator
 * @Date: 2023/5/10 19:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVO extends History {

    @ApiModelProperty(value = "帖子")
    private PostsDetailVO posts;

    @ApiModelProperty(value = "帖子标题")
    private String postTitle;

    @ApiModelProperty(value = "帖子封面路径")
    private String postCoverPath;

    @ApiModelProperty(value = "用户昵称")
    public String nickname;

    @ApiModelProperty(value = "头像")
    public String avatar;
    /**
     * {@link com.love.product.enumerate.PostsType}
     */
    @ApiModelProperty(value = "帖子类型 1闲置帖 2校园帖")
    public Integer postType;

    @ApiModelProperty(value = "校区")
    public String schoolName;
}
