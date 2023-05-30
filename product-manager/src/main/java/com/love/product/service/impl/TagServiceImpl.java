package com.love.product.service.impl;

import com.love.product.mapper.TagMapper;
import com.love.product.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.service
 * @Description: TagServiceImpl
 * @Author: Administrator
 * @Date: 2023/5/23 19:23
 */
@Slf4j
@Service
public class TagServiceImpl  implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Override
    public String getTagNameById(Long tagId) {
        return tagMapper.getTagNameById(tagId);
    }

}
