package com.love.product.entity.req;

import com.love.product.entity.Posts;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjf
 * @date 2022-12-28 9:23
 * @describe 帖子请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsReq extends Posts {

    @ApiModelProperty(value = "上传文件")
    private MultipartFile[] files;

}
