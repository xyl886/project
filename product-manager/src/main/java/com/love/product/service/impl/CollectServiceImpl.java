package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Collect;
import com.love.product.entity.Posts;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.vo.CollectVO;
import com.love.product.entity.vo.PostsVO;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.CollectMapper;
import com.love.product.service.CollectService;
import com.love.product.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Resource
    private PostsService postsService;
    @Resource
    private CollectMapper collectMapper;

    @Override
    public Result<?> add(Long userId, Long postsId,Integer deleted) {
        Posts posts = postsService.getById(postsId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        if(posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())){
            LocalDateTime now = LocalDateTime.now();
            Collect collect = new Collect();
            collect.setId(IdWorker.getId());
            collect.setUserId(userId);
            collect.setPostsId(posts.getId());
            collect.setPostsUserId(posts.getUserId());
            collect.setDeleted(yesOrNo.getValue());
            collect.setCreateTime(now);
            collect.setUpdateTime(now);
            collectMapper.add(collect);
            if(yesOrNo.equals(YesOrNo.YES)){
                return Result.OKMsg("已取消收藏");
            }else{
                return Result.OKMsg("收藏成功");
            }
        }else{
            return Result.failMsg("帖子不存在或已下架");
        }
    }

    @Override
    public Collect getDetail(Long userId, Long postsId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId,userId)
                .eq(Collect::getPostsId,postsId)
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    /**
     * 分页
     */
    @Override
    public ResultPage<CollectVO> getPage(Long userId, PageQuery pageQuery) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId);
        queryWrapper.orderByDesc(Collect::getCreateTime);
        Page<Collect> page = page(pageQuery.build(), queryWrapper);
        List<CollectVO> list = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(collect -> {
                CollectVO collectVO = BeanUtil.copyProperties(collect, CollectVO.class);
                postsIds.add(collectVO.getPostsId());
                return collectVO;
            }).collect(Collectors.toList());
        }
        Map<Long, PostsVO> postsHashMap;
        if(list.size() > 0){
            postsHashMap = postsService.listByIds(postsIds);
            Map<Long, PostsVO> finalPostsHashMap = postsHashMap;
            list.forEach(item -> {
                PostsVO postsVO = finalPostsHashMap.get(item.getPostsId());
                item.setPosts(postsVO);
            });
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }
}
