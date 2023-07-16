package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.MessageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PackageName: com.love.product.mapper
 * @Description: MessageMapper
 * @Author: Administrator
 * @Date: 2023/7/4 11:46
 */

public interface MessageMapper extends BaseMapper<Message> {

    List<MessageDTO> listMessages(@Param("fromId") Long fromId,@Param("toId") Long toId);

}
