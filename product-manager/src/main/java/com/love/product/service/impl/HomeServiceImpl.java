package com.love.product.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.love.product.entity.Posts;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.CategoryPostCountDTO;
import com.love.product.entity.vo.ContributeVO;
import com.love.product.entity.vo.HomeVO;
import com.love.product.entity.vo.TagVO;
import com.love.product.enums.PostStatus;
import com.love.product.enums.Role;
import com.love.product.mapper.*;
import com.love.product.service.HomeService;
import com.love.product.service.RedisService;
import com.love.product.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.love.product.constant.CommonConstant.ak;
import static com.love.product.constant.CommonConstant.sk;
import static com.love.product.enums.PostStatus.PUBLISHED;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: HomeServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/21 14:51
 */
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private PostsCommentMapper postsCommentMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private RedisService redisService;
    /**
     * 文章、留言、用户、已认证
     * @return
     */
    public Result<Map<String,Integer>> lineCount(){
        Map<String,Integer> map = new HashMap<>();
        map.put("postsCount", postsMapper.selectList(null).size());
        map.put("pendingPostsCount", postsMapper.selectList(
                new LambdaQueryWrapper<Posts>()
                        .eq(Posts::getStatus,PostStatus.PENDING.getValue())).size());
        map.put("commentCount",postsCommentMapper.selectList(null).size());
        map.put("userCount",userInfoMapper.selectList(null).size());
        map.put("authCount",userInfoMapper.selectList(
                new LambdaQueryWrapper<UserInfo>()
                        .ne(UserInfo::getRole, Role.VISITOR.getValue())).size());
        map.put("categoryCount",categoryMapper.selectList(null).size());
        return Result.OK(map);
    }

    @Override
    public Result hot(String type) {
        String url = "https://www.coderutil.com/api/resou/v1/" + type;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access-key", ak);
        paramMap.put("secret-key", sk);
        String result= HttpUtil.get(url, paramMap);
        JSONArray data = JSONObject.parseObject(result).getJSONArray("data");
        // 遍历data数组
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            String summary = item.getString("summary");

            // 如果summary为空，则将keyword赋值给summary
            if (summary == null || summary.isEmpty()) {
                String keyword = item.getString("keyword");
                item.put("summary", keyword);
            }
        }
        return Result.OK(data);
    }

    public Result<HomeVO> init() {
        //文章排行
        List<Posts> postsList =postsMapper.selectList(new QueryWrapper<Posts>()
                .orderByDesc("browse_num")
                .eq("status",PUBLISHED.getValue())
                .last("LIMIT 6"));
        //文章贡献度
        Map<String, Object> contribute = this.contribute();
        //分类统计
        List<CategoryPostCountDTO> categoryCount = this.categoryCount();
        //标签统计
        List<TagVO> tagsList = this.tagsCount();
        log.info(String.valueOf(HomeVO.builder()
                .CategoryPostCountDTO(categoryCount)
                .contribute(contribute)
                .posts(postsList)
                .TagVO(tagsList).build()));
        return Result.OK(HomeVO.builder()
                .CategoryPostCountDTO(categoryCount)
                .contribute(contribute)
                .posts(postsList)
                .TagVO(tagsList).build());
    }

    private List<TagVO> tagsCount() {
        return tagsMapper.selectAll();
    }

    private List<CategoryPostCountDTO> categoryCount() {
        return categoryMapper.getCategoryPostCount();
    }

    public Map<String, Object> contribute() {
        Map<String, Object> map = new HashMap<>();
        List<Object> contributes = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        List<String> months = DateUtils.getMonths();
        String lastTime = months.get(0), nowTime = months.get(months.size() - 1);
        List<ContributeVO> posts = postsMapper.contribute(lastTime, nowTime);
        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<ContributeVO> collect = posts.stream().filter(
                    article -> article.getDate()
                            .equals(item))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) list.add(collect.get(0).getCount());
            else list.add(0);
            result.add(list);
        });
        contributes.add(lastTime);
        contributes.add(nowTime);
        map.put("contributeDate", contributes);
        map.put("postContributeCount", result);
        return map;
    }
}
