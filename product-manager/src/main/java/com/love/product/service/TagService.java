package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Tag;

/**
 * @PackageName: com.love.product.service
 * @Description: TagService
 * @Author: Administrator
 * @Date: 2023/5/23 19:23
 */

public interface TagService{
    String getTagNameById(Long tagId);
}
