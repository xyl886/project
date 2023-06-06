package com.love.product.model.VO;

import com.love.product.entity.Collect;
import com.love.product.entity.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-12-28 10:44
 * @describe 收藏
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectVO extends Collect {

    @ApiModelProperty(value = "帖子")
    private PostsDetailVO posts;

    @ApiModelProperty(value = "用户基本信息")
    private UserBasicInfoVO userBasicInfo;
}
