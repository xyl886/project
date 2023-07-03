package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface DictDataService extends IService<DictData> {

    Result listDictData(Integer dictId, Integer isPublish);

    Result insertDictData(DictData dictData);

    Result updateDictData(DictData dictData);

    Result deleteBatch(List<Long> ids);

    Result deleteDictData(Long id);

    Result getDataByDictType(List<String> types);

}
