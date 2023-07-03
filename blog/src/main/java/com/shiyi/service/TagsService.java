package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.Tags;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
public interface TagsService extends IService<Tags> {

    Result listTags(String name);

    Result insertTag(Tags tags);

    Result updateTag(Tags tags);

    Result deleteById(Long id);

    Result deleteBatch(List<Long> ids);

    Result getTagsById(Long id);

    Result top(Long id);


    //    -----web端方法开始-----
    Result webList();

}
