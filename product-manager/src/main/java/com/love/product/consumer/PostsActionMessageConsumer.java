package com.love.product.consumer;

import com.love.product.consumer.message.PostsActionMessage;
import com.love.product.service.PostsService;
import com.love.product.service.RedisService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.*;

import static com.love.product.constant.RabbitMQConstant.POSTS_ACTION_QUEUE;
import static com.love.product.constant.RedisKeyConstant.*;

@Component
public class PostsActionMessageConsumer {

    @Resource
    private PostsService postsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisService redisService;

    @RabbitListener(queues = POSTS_ACTION_QUEUE)
    public void consumeMessage(PostsActionMessage message) {
        switch (message.getActionType()) {
            case BROWSE:
                handleBrowseAction(message.getPostsId());
                break;
            case LIKE:
                handleLikeAction(message.getPostsId());
                break;
            case COMMENT:
                handleCommentAction(message.getPostsId());
                break;
            case COLLECT:
                handleCollectAction(message.getPostsId());
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + message.getActionType());
        }
    }

    private void handleBrowseAction(Long postsId) {
        // 处理浏览操作逻辑，更新帖子浏览数等
        redisService.hIncr(POSTS + postsId, "browseNum", 1L);
    }

    private void handleLikeAction(Long postsId) {
        // 处理点赞操作逻辑，更新帖子点赞数等
        redisService.hIncr(POSTS + postsId, "likeNum", 1L);
    }

    private void handleCommentAction(Long postsId) {
        // 处理评论操作逻辑，更新帖子评论数等
        redisService.hIncr(POSTS + postsId, "commentNum", 1L);
    }

    private void handleCollectAction(Long postsId) {
        // 处理收藏操作逻辑，更新帖子收藏数等
        redisService.hIncr(POSTS + postsId, "collectNum", 1L);
    }
    public void createPost(Long postId, String title) {
        // 执行创建帖子的操作，将帖子的标题和内容存储到数据库中
        // 存储帖子的标题到Redis缓存
        redisService.hSet(POST_TITLES, postId.toString(), title);
    }
    // 计算热度值
    private double calculateHotValue(Long postsId) {
        String key = POSTS + postsId;
        double browseNum = (Double) redisService.hGet(key, "browseNum");
        double likeNum = (Double)  redisService.hGet(key, "likeNum");
        double commentNum = (Double)  redisService.hGet(key, "commentNum");
        double collectNum = (Double)  redisService.hGet(key, "collectNum");
        return browseNum * 0.4 + likeNum * 0.3 + commentNum * 0.2 + collectNum * 0.1;
    }
    // 更新热度排行榜
    private void updateHotList(Long postsId) {
        double hotValue = calculateHotValue(postsId);
        redisService.zSetAdd(HOTLIST, String.valueOf(postsId), hotValue);
    }

    // 获取热度排行榜帖子信息
    public List<Map<Long, String>> getHotPosts(int count) {
        Set<Long> hotPosts = redisService.zSetRange(HOTLIST, 0, count - 1);
        List<Map<Long, String>> hotPostsInfoList = new ArrayList<>();
        for (Long postId : hotPosts) {
            String title = (String) redisService.hGet(POST_TITLES, postId.toString());
            if (title != null) {
                Map<Long, String> hotPostInfo = new HashMap<>();
                hotPostInfo.put(postId, title);
                hotPostsInfoList.add(hotPostInfo);
            }
        }
        return hotPostsInfoList;
    }
}
