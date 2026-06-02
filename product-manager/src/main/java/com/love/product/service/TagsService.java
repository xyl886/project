package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.Tags;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.TagPageReq;
import com.love.product.entity.vo.TagVO;

import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: TagService
 * @Author: Administrator
 * @Date: 2023/5/23 19:23
 */

public interface TagsService extends IService<Tags>{
    String getTagNameById(Long tagId);

    Tags getTagsById(Long id);

    void deleteBatch(List<Long> ids);

    void deleteById(Long id);

    void updateTag(Tags tags);

    void insertTag(Tags tags);

    ResultPage<Tags> listTags(TagPageReq tagPageReq);

    List<TagVO> webList();
}
