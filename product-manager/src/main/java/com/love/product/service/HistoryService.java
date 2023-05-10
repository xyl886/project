package com.love.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.love.product.entity.History;

/**
 * @PackageName: com.love.product.service
 * @Description: HistoryService
 * @Author: Administrator
 * @Date: 2023/4/24 11:17
 */

public interface HistoryService extends IService<History> {

    History findHistory(Long userId, Long id);

    void updateHistory(History existingHistory);

    void saveHistory(History history);
}
