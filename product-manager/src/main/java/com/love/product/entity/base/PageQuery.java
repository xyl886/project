package com.love.product.entity.base;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 分页查询实体类
 */
@Slf4j
@Data
@Accessors(chain = true)
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;

    /**
     * 当前页数
     */
    @ApiModelProperty("当前页数")
    private Integer currentPage = 1;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 最大每页条目数
     */
    private static final int MAX_PAGE_SIZE = 100;


    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getCurrentPage(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        pageNum = Math.max(pageNum, DEFAULT_PAGE_NUM);
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        return new Page<>(pageNum, pageSize);
    }

    @ApiModelProperty(hidden = true)
    public int getEsFrom() {
        return 2 <= currentPage ? (currentPage - 1) * pageSize : 0;
    }
}
