package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Tag;

/**
 * @PackageName: com.love.product.mapper
 * @Description: TagMapper
 * @Author: Administrator
 * @Date: 2023/5/23 19:25
 */

public interface TagMapper extends BaseMapper<Tag> {
     String getTagNameById(Long tagId) ;

}
