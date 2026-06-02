package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.CommentLike;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsComment;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.CommentPageReq;
import com.love.product.entity.vo.CommentVO;
import com.love.product.entity.vo.PostsCommentVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enums.Role;
import com.love.product.enums.YesOrNo;
import com.love.product.mapper.CommentLikeMapper;
import com.love.product.mapper.PostsCommentMapper;
import com.love.product.mapper.PostsMapper;
import com.love.product.mapper.UserInfoMapper;
import com.love.product.service.PostsCommentService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
import com.love.product.util.XssUtils;
import com.love.product.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostsCommentServiceImpl extends ServiceImpl<PostsCommentMapper, PostsComment> implements PostsCommentService {

    @Resource
    private PostsService postsService;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CommentLikeMapper commentLikeMapper;
    @Resource
    private NotificationService notificationService;
    @Override
    public void add(Long userId, Long postsId, String content, Long parentCommentId) {
        log.info(content);
        Posts posts = postsService.getById(postsId);
        if(posts == null || posts.getStatus().equals(YesOrNo.YES.getValue())){
            throw new BizException("帖子不存在或已下架");
        }
        PostsComment comment = new PostsComment();
        comment.setUserId(userId);
        comment.setPostsId(posts.getId());
        comment.setContent(XssUtils.filter(content));
        if (parentCommentId != null) {
            comment.setParentId(parentCommentId);
        }
        save(comment);
        int commentNum = posts.getCommentNum();
        commentNum = commentNum + 1;
        if(commentNum < 0){
            commentNum = 0;
        }
        posts.setCommentNum(commentNum);
        //更新评论数
        postsService.saveOrUpdate(posts);
        //通知帖子作者
        if (!posts.getUserId().equals(userId))
            notificationService.notify(posts.getUserId(), userId, 2, "评论了你的帖子", postsId, 1);
        //通知被回复的人
        if (parentCommentId != null) {
            PostsComment parent = getById(parentCommentId);
            if (parent != null && !parent.getUserId().equals(userId))
                notificationService.notify(parent.getUserId(), userId, 3, "回复了你的评论", parentCommentId, 2);
        }
    }

    // 获取当前用户对这些评论的点赞情况
    public Map<Long, CommentLike> mapByCommentIdsAndUserId(List<Long> commentIds, Long userId) {
        if (commentIds == null || commentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<CommentLike> commentLikes = commentLikeMapper.selectList(new QueryWrapper<CommentLike>()
                .in("comment_id", commentIds)
                .eq("user_id", userId));
        return commentLikes.stream()
                .collect(Collectors.toMap(CommentLike::getCommentId, Function.identity()));
    }
    @Override
    public List<PostsCommentVO> listByPostsId(Long postsId) {
        List<PostsCommentVO> commentVOS = new ArrayList<>();
        LambdaQueryWrapper<PostsComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsComment::getPostsId,postsId)
        .orderByDesc(PostsComment::getCreateTime);
        List<PostsComment> comments = list(queryWrapper);

        // 获取当前用户对这些评论的点赞情况
        List<Long> commentIds = comments.stream().map(PostsComment::getId).collect(Collectors.toList());
        Map<Long, CommentLike> commentLikeMap = mapByCommentIdsAndUserId(commentIds, JwtUtil.getUserId());

        List<Long> userIds = comments.stream().map(PostsComment::getUserId).collect(Collectors.toList());
        Map<Long, UserInfoVO> userInfoVOMap = new HashMap<>();
        if(userIds.size() > 0){
            userInfoVOMap = userInfoService.listByIds(userIds);
        }
        Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
        comments.forEach(item -> {
            PostsCommentVO VO = BeanUtil.copyProperties(item, PostsCommentVO.class);
            UserInfoVO userInfoVO = finalUserInfoVOMap.get(item.getUserId());
            UserBasicInfoVO userBasicInfoVO=new UserBasicInfoVO();
            BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
            userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
            VO.setUserInfo(userBasicInfoVO);
            commentVOS.add(VO);
            VO.setLike(false);
            CommentLike commentLike = commentLikeMap.get(item.getId());
            if(commentLike != null){
                VO.setLike(true);
            }
        });
        return commentVOS;
    }

    @Override
    public void del(Long userId, Long id) {
        PostsComment comment = getById(id);
        if(comment == null){
            throw new BizException("评论不存在或已删除");
        }
        if(!comment.getUserId().equals(userId)){
            throw new BizException("抱歉，您无权操作");
        }
        removeById(id);
        Posts posts = postsService.getById(comment.getPostsId());
        int commentNum = posts.getCommentNum();
        commentNum = commentNum - 1;
        if(commentNum < 0){
            commentNum = 0;
        }
        posts.setCommentNum(commentNum);
        //更新评论数
        postsService.saveOrUpdate(posts);
    }

    @Override
    public void addCommentLike(Long userId, Long commentId, Integer deleted) {
        PostsComment comment = getById(commentId);
        YesOrNo yesOrNo = YesOrNo.valueOf(deleted);
        if(yesOrNo == null){
            yesOrNo = YesOrNo.NO;
        }
        // 为用户创建新的点赞对象并将其添加到评论中
        CommentLike like = new CommentLike();
        like.setUserId(userId);
        like.setCommentId(commentId);
        like.setDeleted(yesOrNo.getValue());
        // 将点赞记录保存到数据库
        // 检查用户是否已经点过赞
        boolean hasLiked = commentLikeMapper.selectCount(new QueryWrapper<CommentLike>()
                .eq("user_id", userId)
                .eq("comment_id", commentId)
                .last("LIMIT 1")) > 0;

        if (hasLiked) {
            commentLikeMapper.update(like,new UpdateWrapper<CommentLike>()
                    .eq("user_id", userId)
                    .eq("comment_id", commentId)
                    .last("LIMIT 1"));
        }else {
            commentLikeMapper.insert(like);
        }
        if(yesOrNo.equals(YesOrNo.YES)){
            comment.likeNum -= 1;
        }else {
            comment.likeNum += 1;
        }
        // 增加点赞计数并保存评论
        if (comment.likeNum < 0) {
            comment.likeNum=0;
        }
        saveOrUpdate(comment);
    }
    @Override
    public ResultPage<CommentVO> listComment(CommentPageReq commentPageReq) {
        Page<CommentVO> Page = baseMapper.selectPageList(
                new Page<>(commentPageReq.getCurrentPage(), commentPageReq.getPageSize()), commentPageReq);
        List<CommentVO> list=new ArrayList<>();
        if (Page.getTotal() > 0) {
            list = Page.getRecords();
        }
        return ResultPage.OK(Page.getTotal(), Page.getCurrent(), Page.getSize(), list);
    }
    @Override
    public void deleteComment(Long id) {
        PostsComment comment = getById(id);
        if(comment == null){
            throw new BizException("评论不存在或已删除");
        }
        removeById(id);
        Posts posts = postsService.getById(comment.getPostsId());
        int commentNum = posts.getCommentNum();
        commentNum = commentNum - 1;
        if(commentNum < 0){
            commentNum = 0;
        }
        posts.setCommentNum(commentNum);
        //更新评论数
        postsService.saveOrUpdate(posts);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
