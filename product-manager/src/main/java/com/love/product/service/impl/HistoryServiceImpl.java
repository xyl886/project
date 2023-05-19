package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Collect;
import com.love.product.entity.History;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.CollectVO;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.entity.vo.PostsVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.HistoryMapper;
import com.love.product.service.HistoryService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: HistoryServiceImpl
 * @Author: Administrator
 * @Date: 2023/4/24 11:18
 */
@Slf4j
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {
    @Resource
    private PostsService postsService;
    @Resource
    private UserInfoService userInfoService;
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

    @Override
    public ResultPage<HistoryVO> getPage(Long userId, PageQuery pageQuery) {
        LambdaQueryWrapper<History> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(History::getUserId, userId);
        queryWrapper.orderByDesc(History::getUpdateTime);
        Page<History> page = page(pageQuery.build(), queryWrapper);
        List<HistoryVO> list = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(history -> {
                HistoryVO historyVO = BeanUtil.copyProperties(history, HistoryVO.class);
                postsIds.add(historyVO.getPostsId());
                return historyVO;
            }).collect(Collectors.toList());
        }
        // 获取帖子信息
        Map<Long, PostsVO> postsHashMap;
        if (!postsIds.isEmpty()) {
            postsHashMap = postsService.listByIds(postsIds);
            for (HistoryVO historyVO : list) {
                PostsVO postsVO = postsHashMap.get(historyVO.getPostsId());
                // 设置帖子标题和封面路径
                if (postsVO != null) {
                    historyVO.setPostTitle(postsVO.getTitle());
                    historyVO.setPostCoverPath(postsVO.getCoverPath());
                    historyVO.setPostType(postsVO.getPostsType());
                    historyVO.setPosts(postsVO);
                }
            }
        }
        // 获取用户nickname
        for (HistoryVO historyVO : list) {
            UserInfoVO userInfoVO = userInfoService.getUserInfoById(historyVO.getUserId());
            historyVO.setNickname(userInfoVO.getNickname());
            historyVO.setAvatar(userInfoVO.getAvatar());
        }
        log.info("浏览记录："+list);
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public Result<?> del(Long userId, Long id) {
        return null;
    }
}
