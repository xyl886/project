package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.HistoryPageReq;
import com.love.product.entity.vo.HistoryVO;
import com.love.product.entity.vo.PostsDetailVO;
import com.love.product.enumerate.LikeStatus;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.PostsLikeMapper;
import com.love.product.service.*;
import com.love.product.util.RedisKeyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostsLikeServiceImpl extends ServiceImpl<PostsLikeMapper, PostsLike> implements PostsLikeService {
    @Resource
    private PostsService postsService;
    @Resource
    private PostsLikeMapper postsLikeMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private FileUploadService fileUploadService;


    @Override
    public Result<?> add(Long userId, Long postsId, Integer deleted) {
        Posts posts = postsService.getById(postsId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        if(posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())){
            LocalDateTime now = LocalDateTime.now();

            String key = RedisKeyUtils.getLikedKey(userId, postsId);
            redisService.hSet(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikeStatus.LIKE.getCode());

            // 发送点赞消息到RabbitMQ
            Map<String, Object> Rmsg = new HashMap<>();
            Rmsg.put("userId", userId);
            Rmsg.put("postsId", postsId);
            Rmsg.put("deleted", deleted);
            rabbitTemplate.convertAndSend("posts_like", Rmsg);

            PostsLike postsLike = new PostsLike();
            postsLike.setId(IdWorker.getId());
            postsLike.setUserId(userId);
            postsLike.setPostsId(posts.getId());
            postsLike.setPostsUserId(posts.getUserId());
            postsLike.setDeleted(yesOrNo.getValue());
            postsLike.setCreateTime(now);
            postsLike.setUpdateTime(now);
            postsLikeMapper.add(postsLike);
            String msg = "点赞成功";
            int likeNum = posts.getLikeNum();
            if(yesOrNo.equals(YesOrNo.YES)){
                likeNum = likeNum - 1;
                postsService.saveOrUpdate(posts);
                msg = "已取消点赞";
            }else{
                likeNum = likeNum + 1;
            }
            if(likeNum < 0){
                likeNum = 0;
            }
            posts.setLikeNum(likeNum);
            //更新点赞数
            postsService.saveOrUpdate(posts);
            return Result.OKMsg(msg);
        }else{
            return Result.failMsg("帖子不存在或已下架");
        }
    }

    @Override
    public PostsLike getDetail(Long userId, Long postsId) {
        LambdaQueryWrapper<PostsLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsLike::getUserId,userId)
                .eq(PostsLike::getPostsId,postsId)
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public Map<Long, PostsLike> listByUserId(Long userId,List<Long> postsIds){
        Map<Long, PostsLike> postsLikeHashMap = new HashMap<>();
        if(userId == null || postsIds.size() ==0 ){
            return new HashMap<>();
        }
        LambdaQueryWrapper<PostsLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsLike::getUserId,userId);
        queryWrapper.in(PostsLike::getPostsId,postsIds);
        List<PostsLike> likes = list(queryWrapper);
        likes.forEach(item -> {
            postsLikeHashMap.put(item.getPostsId(), item);
        });
        return postsLikeHashMap;
    }

    @Override
    public ResultPage<HistoryVO> getPage(Long userId, HistoryPageReq pageQuery) {
            Page<HistoryVO> Page = baseMapper.selectPageList(
                    new Page<>(pageQuery.getCurrentPage(), pageQuery.getPageSize()), pageQuery,userId);
            List<HistoryVO> list=new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
            if (Page.getTotal() > 0) {
                list = Page.getRecords().stream().peek(historyVO ->
                        postsIds.add(historyVO.getPostsId())
                        ).collect(Collectors.toList());
            }
        // 获取帖子信息
        getHVOPosts(list, postsIds, postsService, categoryService);
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }

    static void getHVOPosts(List<HistoryVO> list, List<Long> postsIds, PostsService postsService, CategoryService categoryService) {
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
                    historyVO.setSchoolName(
                            categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
                }
            }
        }
    }
}
