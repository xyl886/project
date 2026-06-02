package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.Collect;
import com.love.product.entity.Posts;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CollectPageReq;
import com.love.product.enums.PostStatus;
import com.love.product.enums.Role;
import com.love.product.enums.YesOrNo;
import com.love.product.mapper.CollectMapper;
import com.love.product.entity.vo.CollectVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.PostsMapper;
import com.love.product.service.CategoryService;
import com.love.product.service.CollectService;
import com.love.product.service.NotificationService;
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
    private CategoryService categoryService;
    @Resource
    private PostsService postsService;
    @Resource
    private UserInfoServiceImpl userInfoService;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private NotificationService notificationService;
    @Override
    public void add(Long userId, Integer deleted, Long... postsIds) {
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if (yesOrNo == null) {
            yesOrNo = YesOrNo.NO;
        }
        LocalDateTime now = LocalDateTime.now();
        int successCount = 0;

        for (Long postsId : postsIds) {
            Posts posts = postsMapper.getPostsById(postsId);
            if (posts != null) {
                Collect collect = new Collect();
                collect.setId(IdWorker.getId());
                collect.setUserId(userId);
                collect.setPostsId(posts.getId());
                collect.setDeleted(yesOrNo.getValue());
                collect.setCreateTime(now);
                collect.setUpdateTime(now);
                collectMapper.add(collect);
                if (yesOrNo.equals(YesOrNo.YES)) {
                    posts.setCollectNum(Math.max(posts.getCollectNum() - 1, 0));
                    postsService.updatePostsCollectNum(posts.id, posts.collectNum);
                } else {
                    posts.setCollectNum(posts.getCollectNum() + 1);
                    postsService.updatePostsCollectNum(posts.id, posts.collectNum);
                }
                successCount++;
            }
        }
        if (successCount == 0) {
            throw new BizException("收藏失败，帖子不存在");
        }
        for (Long pid : postsIds) { Posts post = postsMapper.getPostsById(pid); if (post != null && !post.getUserId().equals(userId)) notificationService.notify(post.getUserId(), userId, 5, "收藏了你的帖子", pid, 1); }
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
    public ResultPage<CollectVO> getPage(Long userId, CollectPageReq pageQuery) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId)
                .orderByDesc(Collect::getCreateTime);
        if (pageQuery.getCategoryId() != null) {
            queryWrapper.apply("posts_id IN (SELECT id FROM s_posts WHERE category_id = {0})",
                    pageQuery.getCategoryId());
        }
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
                log.info(String.valueOf(postsDetailVO));
                if(postsDetailVO!=null){
                    postsDetailVO.setCategoryName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.categoryId)));
                    postsDetailVO.setPostStatus(PostStatus.valueOf(postsDetailVO.status).getText());
                    item.setPosts(postsDetailVO);
                }else {
                    item.setPosts(new PostsDetailVO());
                }
            });
        }
        // 关联查询用户信息
        List<Long> userIds = list.stream().map(CollectVO::getUserId).collect(Collectors.toList());
        log.info(userIds.toString());
        if(list.size() > 0) {
            Map<Long, UserInfoVO> users = userInfoService.listByIds(userIds);
            list.forEach(item -> {
                UserInfoVO user = users.get(item.getUserId());
                if (user != null) {
                    UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
                    BeanUtil.copyProperties(user, userBasicInfoVO);
                    userBasicInfoVO.setRole(Role.valueOf(user.getRole()).getText());
                    item.setUserInfo(userBasicInfoVO);
                }
            });
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public void deleteBatch(Long userId, List<Long> postsIds) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId)
                .in(Collect::getPostsId,postsIds);
        boolean rows = remove(queryWrapper);
        if (!rows) {
            throw new BizException("批量取消失败");
        }
    }
}
