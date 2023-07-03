package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface DictService extends IService<Dict> {

    Result listDict(String name, Integer isPublish, String descColumn, String ascColumn);

    Result insertDict(Dict dict);

    Result updateDict(Dict dict);

    Result deleteDict(int id);

    Result deleteBatch(List<Long> list);

}
