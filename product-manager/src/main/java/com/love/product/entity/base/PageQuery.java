package com.love.product.entity.base;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页查询实体类
 */
@Data
@Accessors(chain = true)
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    @ApiModelProperty("分页大小")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    @ApiModelProperty("当前页数")
    private Integer currentPage = DEFAULT_PAGE_NUM;

    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getCurrentPage(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        pageNum = Math.max(pageNum, DEFAULT_PAGE_NUM);
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        return new Page<>(pageNum, pageSize);
    }
}
