package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsComment;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.PostsCommentVO;
import com.love.product.entity.vo.UserBasicInfoVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.PostsCommentMapper;
import com.love.product.service.PostsCommentService;
import com.love.product.service.PostsService;
import com.love.product.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class PostsCommentServiceImpl extends ServiceImpl<PostsCommentMapper, PostsComment> implements PostsCommentService {

    @Resource
    private PostsService postsService;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public Result<?> add(Long userId, Long postsId, String content) {
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

    @Override
    public Result<List<PostsCommentVO>> listByPostsId(Long postsId) {
        List<PostsCommentVO> commentVOS = new ArrayList<>();
        LambdaQueryWrapper<PostsComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostsComment::getPostsId,postsId)
        .orderByDesc(PostsComment::getCreateTime);
        List<PostsComment> comments = list(queryWrapper);
        List<Long> userIds = comments.stream().map(PostsComment::getUserId).collect(Collectors.toList());
        Map<Long, UserInfoVO> userInfoVOMap = new HashMap<>();
        if(userIds.size() > 0){
            userInfoVOMap = userInfoService.listByIds(userIds);
        }
        Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
        comments.forEach(item -> {
            PostsCommentVO VO = BeanUtil.copyProperties(item, PostsCommentVO.class);
            UserInfoVO userInfoVO = finalUserInfoVOMap.get(item.getUserId());
            VO.setUserBasicInfoVO(new UserBasicInfoVO(userInfoVO.getId(),userInfoVO.getNickname(),userInfoVO.getAvatar()));
            commentVOS.add(VO);
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
}
