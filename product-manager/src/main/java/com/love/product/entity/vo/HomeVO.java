package com.love.product.entity.vo;

import com.love.product.entity.Posts;
import com.love.product.entity.dto.CategoryPostCountDTO;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @PackageName: com.love.product.entity.vo
 * @Description: HomeVO
 * @Author: Administrator
 * @Date: 2023/7/24 9:34
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeVO {

  private List<TagVO> TagVO;

  private List<CategoryPostCountDTO> CategoryPostCountDTO;

  private Map<String, Object> contribute;

  private  List<Posts> posts;
}
