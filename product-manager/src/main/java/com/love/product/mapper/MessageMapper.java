package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PackageName: com.love.product.mapper
 * @Description: MessageMapper
 * @Author: Administrator
 * @Date: 2023/7/4 11:46
 */

public interface MessageMapper extends BaseMapper<Message> {

    void passBatch(@Param("ids") List<Integer> ids);
}
