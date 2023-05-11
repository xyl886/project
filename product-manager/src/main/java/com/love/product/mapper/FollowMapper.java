package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Follow;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
public interface FollowMapper extends BaseMapper<Follow> {

    int add(Follow follow);

}
