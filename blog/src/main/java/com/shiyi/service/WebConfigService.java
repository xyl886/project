package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站配置表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
public interface WebConfigService extends IService<WebConfig> {

    Result listWebConfig();

    Result updateWebConfig(WebConfig webConfig);
}
