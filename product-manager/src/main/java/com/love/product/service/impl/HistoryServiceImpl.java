package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.History;
import com.love.product.mapper.HistoryMapper;
import com.love.product.service.HistoryService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: HistoryServiceImpl
 * @Author: Administrator
 * @Date: 2023/4/24 11:18
 */
@Slf4j
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    @Override
    public History findHistory(Long userId, Long id) {
        LambdaQueryWrapper<History> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(History::getUserId,userId).eq(History::getPostsId,id);
        History  history= getOne(queryWrapper);
          if (getOne(queryWrapper)!=null){
              log.info("之前浏览过"+history);
              return history;
          }
        return null;
    }

    @Override
    public void updateHistory(History existingHistory) {
        log.info("浏览更新时间"+ existingHistory);
          updateById(existingHistory);
    }

    @Override
    public void saveHistory(History history) {
        log.info("浏览了"+history);
           saveOrUpdate(history);
    }
}
