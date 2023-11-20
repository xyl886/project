package com.love.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.dto.ChatMessageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PackageName: com.love.product.mapper
 * @Description: ChatMessageMapper
 * @Author: Administrator
 * @Date: 2023/8/7 11:23
 */

public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    List<ChatMessageDTO> listMessages(@Param("fromId") Long fromId, @Param("toId") Long toId);
}
