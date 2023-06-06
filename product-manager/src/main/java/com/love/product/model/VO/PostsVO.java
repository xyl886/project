package com.love.product.model.VO;

import com.love.product.entity.Posts;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @date 2022-12-28 9:23
 * @describe 帖子请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsVO extends Posts {

    @ApiModelProperty(value = "新上传文件")
    private MultipartFile[] Files;

    @ApiModelProperty(value = "上传文件")
    private String oldFiles;

    @ApiModelProperty(value = "移除的文件")
    private String removeFiles;
}
