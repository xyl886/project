package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.CommentLike;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsComment;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.PostsCommentVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.Role;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.CommentLikeMapper;
import com.love.product.mapper.PostsCommentMapper;
import com.love.product.service.PostsCommentService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import com.love.product.util.JwtUtil;
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
    private UserInfoService userInfoService;
    @Resource
    private CommentLikeMapper commentLikeMapper;
    @Override
    public Result<?> add(Long userId, Long postsId, String content) {
        log.info(content);
        Posts posts = postsService.getById(postsId);
        if(posts != null && !posts.getStatus().equals(YesOrNo.YES.getValue())){
            PostsComment comment = new PostsComment();
            comment.setUserId(userId);
            comment.setPostsId(posts.getId());
            comment.setPostsUserId(posts.getUserId());
            comment.setContent(content);
            save(comment);
            int commentNum = posts.getCommentNum();
            commentNum = commentNum + 1;
            if(commentNum < 0){
                commentNum = 0;
            }
            posts.setCommentNum(commentNum);
            //更新评论数
            postsService.saveOrUpdate(posts);
            return Result.OK("评论成功",comment);
        }else{
            return Result.failMsg("帖子不存在或已下架");
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
    public Result<List<PostsCommentVO>> listByPostsId(Long postsId) {
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
        return Result.OK(commentVOS);
    }

    @Override
    public Result<?> del(Long userId, Long id) {
        PostsComment comment = getById(id);
        if(comment != null){
            if(comment.getUserId().equals(userId)){
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
                return Result.OK("删除成功",comment);
            }else{
                return Result.failMsg("抱歉，您无权操作");
            }
        }else{
            return Result.failMsg("评论不存在或已删除");
        }
    }

    @Override
    public Result<?> addCommentLike(Long userId, Long commentId, Integer deleted) {
        PostsComment comment = getById(commentId);
        // 检查用户是否已经点过赞
        boolean hasLiked = commentLikeMapper.selectCount(new QueryWrapper<CommentLike>()
                .eq("user_id", userId)
                .eq("comment_id", commentId)
                .last("LIMIT 1")) > 0;

        if (hasLiked) {
            return Result.failMsg("您已经点过赞了！");
        }
        // 为用户创建新的点赞对象并将其添加到评论中
        CommentLike like = new CommentLike();
        like.setUserId(userId);
        like.setCommentId(commentId);
        like.setDeleted(deleted);
        // 将点赞记录保存到数据库
        commentLikeMapper.insert(like);
        comment.likeNum += 1;
        // 增加点赞计数并保存评论
        if (comment.likeNum < 0) {
            comment.likeNum=0;
        }
        saveOrUpdate(comment);
        return Result.OK("点赞成功！");
    }
}
