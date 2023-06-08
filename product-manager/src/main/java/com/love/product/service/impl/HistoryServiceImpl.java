package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.History;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.enumerate.School;
import com.love.product.model.VO.HistoryVO;
import com.love.product.model.VO.PostsDetailVO;
import com.love.product.model.VO.UserInfoVO;
import com.love.product.mapper.HistoryMapper;
import com.love.product.service.HistoryService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
        LambdaQueryWrapper<History> queryWrapper=new LambdaQueryWrapper<>();
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
        queryWrapper.eq(History::getUserId, userId)
                .orderByDesc(History::getUpdateTime);
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
        Map<Long, PostsDetailVO> postsHashMap;
        if (!postsIds.isEmpty()) {
            postsHashMap = postsService.listByIds(postsIds);
            for (HistoryVO historyVO : list) {
                PostsDetailVO postsDetailVO = postsHashMap.get(historyVO.getPostsId());
                // 设置帖子标题和封面路径
                if (postsDetailVO != null) {
                    historyVO.setPostTitle(postsDetailVO.getTitle());
                    historyVO.setPostCoverPath(postsDetailVO.getCoverPath());
                    historyVO.setPostType(postsDetailVO.getPostsType());
                    historyVO.setPosts(postsDetailVO);
                    historyVO.setSchoolName(School.valueOf(postsDetailVO.getSchool()).getText());
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

    /**
     * 删除历史记录
     * @param userId 用户id
     * @param ids 帖子ids
     * @return Result<?>
     */
    @Override
    public Result<?> del(Long userId, Long... ids) {
        List<History> histories = listByIds(Arrays.asList(ids));
        if (histories.isEmpty()) {
            return Result.failMsg("未找到历史记录!");
        }
        for (History history : histories) {
            if (!history.getUserId().equals(userId)) {
                return Result.failMsg("您无权删除此历史记录!");
            }
        }
        boolean success = remove(new QueryWrapper<History>()
                .eq("user_id", userId)
                .in("id", ids));
        if (success) {
            return Result.OK("删除成功!");
        } else {
            return Result.failMsg("删除失败!");
        }
    }
}
