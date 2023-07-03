package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface SystemConfigService extends IService<SystemConfig> {

    Result getConfig();

    Result updateConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
