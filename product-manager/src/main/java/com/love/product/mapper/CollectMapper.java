package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Collect;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 */
public interface CollectMapper extends BaseMapper<Collect> {

    int add(Collect collect);

}
