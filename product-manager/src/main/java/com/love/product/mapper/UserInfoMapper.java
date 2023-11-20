package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.UserInfo;
import org.apache.ibatis.annotations.MapKey;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户mapper
 */

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @MapKey("MONTH")
    List<Map<String, Object>> contribute();
}
