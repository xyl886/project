package com.love.product.entity.dto;

import lombok.Data;

/**
 * 搜索关键词高亮
 * @author lacy
 */

@Data
public class PostEsHighlightData {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
}
