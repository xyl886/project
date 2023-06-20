package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Collect;
import com.love.product.entity.Posts;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.enumerate.Role;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.CollectMapper;
import com.love.product.entity.vo.CollectVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.PostsMapper;
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
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Resource
    private  CollectService collectService;
    @Resource
    private PostsService postsService;
    @Resource
    private UserInfoServiceImpl userInfoService;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private PostsMapper postsMapper;
    @Override
    public Result<?> add(Long userId, Long postsId,Integer deleted) {
        Posts posts = postsMapper.getPostsById(postsId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        if(posts != null){ // && !posts.getStatus().equals(YesOrNo.YES.getValue())
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
             posts.setCollectNum(Math.max(posts.collectNum - 1, 0));
                postsService.updatePostsCollectNum(posts);
                return Result.OKMsg("已取消收藏");
            }else{
                posts.setCollectNum(posts.collectNum+1);
                postsService.updatePostsCollectNum(posts);
                return Result.OKMsg("收藏成功");
            }
        }else{
            LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Collect::getUserId,userId)
                    .eq(Collect::getPostsId,postsId)
                    .last("LIMIT 1");
            collectService.remove(queryWrapper);
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
        Map<Long, PostsDetailVO> postsHashMap;
        if(list.size() > 0){
            postsHashMap = postsService.listByIds(postsIds);
            Map<Long, PostsDetailVO> finalPostsHashMap = postsHashMap;
            list.forEach(item -> {
                PostsDetailVO postsDetailVO = finalPostsHashMap.get(item.getPostsId());
                item.setPosts(postsDetailVO);
            });
        }
        // 关联查询用户信息
        List<Long> userIds = list.stream().map(CollectVO::getUserId).collect(Collectors.toList());
        Map<Long, UserInfoVO> users = userInfoService.listByIds(userIds);
        list.forEach(item -> {
            UserInfoVO user = users.get(item.getUserId());
            if (user != null) {
                item.setUserInfo(new UserBasicInfoVO(user.id, user.nickname, user.avatar, Role.valueOf(user.role).getText()));
            }
        });
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }
}
