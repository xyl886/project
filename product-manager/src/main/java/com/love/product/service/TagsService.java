package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Tags;
import com.love.product.entity.base.Result;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: TagService
 * @Author: Administrator
 * @Date: 2023/5/23 19:23
 */

public interface TagsService extends IService<Tags>{
    String getTagNameById(Long tagId);

    Result getTagsById(Long id);

    Result deleteBatch(List<Long> ids);

    Result deleteById(Long id);

    Result updateTag(Tags tags);

    Result insertTag(Tags tags);

    Result listTags(String name);

    Result webList();
}
