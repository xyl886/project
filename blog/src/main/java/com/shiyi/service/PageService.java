package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-26
 */
public interface PageService extends IService<Page> {

    Result listPage();

    Result insertPage(Page page);

    Result updatePage(Page page);

    Result deletePageById(Long id);
}
