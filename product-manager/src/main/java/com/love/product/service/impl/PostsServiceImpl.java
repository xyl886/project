package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.History;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.Tags;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.*;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.*;
import com.love.product.enums.PostStatus;
import com.love.product.enums.PostsType;
import com.love.product.enums.Role;
import com.love.product.mapper.PostsMapper;
import com.love.product.mapper.TagsMapper;
import com.love.product.service.*;
import com.love.product.util.JwtUtil;
import com.love.product.util.XssUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.love.product.constant.CommonConstant.ALIYUNOSS_PREFIX;


/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements PostsService {

    @Resource
    private PostsMapper postsMapper;
    @Resource
    private TagsService tagsService;
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private HistoryService historyService;

    @Resource
    private OssService ossService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CollectService collectService;

    @Resource
    private FollowService followService;

    @Resource
    private PostsLikeService postsLikeService;

    @Resource
    private PostsService postsService;
    @Resource
    private CategoryService categoryService;





    /**
     * 发布帖子
     */
    @SneakyThrows
    @Override
    public Result<Posts> add(PostsVO postsVO) {
        PostsType postsType = PostsType.valueOf(postsVO.getPostsType());
        List<String> imgPathList = new ArrayList<>();
        if (postsType.equals(PostsType.LEAVE)) {//闲置帖
            if (postsVO.getPrice() == null || postsVO.getPrice().doubleValue() <= 0) {
                return Result.failMsg("请正确输入价格");
            }
            if (postsVO.getFiles() == null || postsVO.getFiles().length == 0) {
                return Result.failMsg("请至少上传一张图片");
            }
        }
        if (postsVO.getFiles() != null) {
            if (postsVO.getFiles().length > 9) {
                return Result.failMsg("最多可上传9张图片");
            }
            //上传图片
            for (MultipartFile multipartFile : postsVO.getFiles()) {
                String imgPath = ossService.uploadFile(multipartFile);        //oss对象存储
                imgPathList.add(imgPath);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Posts posts = new Posts();
        BeanUtil.copyProperties(postsVO, posts);
        // XSS 防护：过滤标题和内容中的危险 HTML
        if (posts.getTitle() != null) {
            posts.setTitle(XssUtils.filter(posts.getTitle()));
        }
        if (posts.getContent() != null) {
            posts.setContent(XssUtils.filter(posts.getContent()));
        }
        posts.setId(IdWorker.getId());
        posts.setUserId(JwtUtil.getUserId());
        posts.setCreateTime(now);
        posts.setUpdateTime(now);
        //审核内容
//        if(discernSensitiveWords(postsVO.content,null)) {
//            posts.setStatus(PostStatus.PUBLISHED.getValue());
//        }else{
//            posts.setStatus(PostStatus.PENDING.getValue());
//        }
        posts.setStatus(PostStatus.PENDING.getValue());
        if (!imgPathList.isEmpty()) {
            posts.setCoverPath(imgPathList.get(0));
            posts.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        //添加标签idList
        List<Long> tagIdList = getTagsList(postsVO);
        boolean flag = save(posts);
        log.info(tagIdList.toString());
        if (flag) {
            if (!tagIdList.isEmpty()) tagsMapper.savePostsTags(posts.id, tagIdList);
            return Result.OK("已提交，等待管理员审核！", posts);
        }
        return Result.failMsg("发布失败，请重试");
    }

    /**
     * 将数据库不存在的标签新增
     *
     * @param postsVO
     * @return
     */
    private List<Long> getTagsList(PostsVO postsVO) {
        List<Long> tagIdList = new ArrayList<>();
        if (StringUtils.isBlank(postsVO.getTags())) return tagIdList;
        List<String> tag = new ArrayList<>(Arrays.asList(postsVO.getTags().split(",")));
        tag.forEach(item -> {
            Tags tags = tagsMapper.selectOne(new QueryWrapper<Tags>().eq("tag_name", item));
            if (tags == null) {
                tags = Tags.builder().id(IdWorker.getId()).tagName(item).build();
                tagsMapper.insert(tags);
            }
            log.info(String.valueOf(tags));
            tagIdList.add(tags.id);
        });
        return tagIdList;
    }

    /**
     * 分页
     *
     * @param postsPageReq
     * @return
     */
    @Override
    public ResultPage<PostsDetailVO> getPage(PostsPageReq postsPageReq) {
        Long userId = JwtUtil.getUserId();
        List<Long> followedUserIds = followService.getFollowedUserIdsByUserId(userId);
        if (Objects.equals(postsPageReq.getCategoryId(), 1) && followedUserIds.isEmpty()) {
            return ResultPage.OK(0, 1, 10, (Collection<PostsDetailVO>) null);
        }
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(postsPageReq.getPostsType() != null, Posts::getPostsType, postsPageReq.getPostsType())//帖子类型不为空，则加入一个等于帖子类型的条件
                .eq(postsPageReq.getCategoryId() != null && postsPageReq.getCategoryId() != 1 && postsPageReq.getCategoryId() != -1,
                        Posts::getCategoryId, postsPageReq.getCategoryId()) //非关注
                .inSql(postsPageReq.getTagId() != null, Posts::getId,
                        "SELECT pt.posts_id FROM s_posts_tag pt WHERE pt.tag_id = " + postsPageReq.getTagId())
                .eq(Objects.equals(postsPageReq.getCategoryId(), -1), Posts::getUserId, userId) //我的帖子
                .like(StringUtils.isNotBlank(postsPageReq.getTitle()), Posts::getTitle, postsPageReq.getTitle())//标题模糊查询
                .in(Objects.equals(postsPageReq.getCategoryId(), 1), Posts::getUserId, followedUserIds)  //关注帖
                .eq(postsPageReq.getStatus() != null, Posts::getStatus, postsPageReq.getStatus())  //正常贴
                .orderByDesc(Posts::getCreateTime)
                .or().like(StringUtils.isNotBlank(postsPageReq.getTitle()), Posts::getDescription, postsPageReq.getTitle());
        log.info(String.valueOf(postsPageReq));
        Page<Posts> page = page(postsPageReq.build(), queryWrapper);

        List<PostsDetailVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(posts -> {
                setCommentAndLike(posts);
                PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
                postsDetailVO.setCategoryName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.categoryId)));
                postsDetailVO.setTags(tagsMapper.selectByPostId(posts.id));
                postsDetailVO.setPostStatus(PostStatus.valueOf(postsDetailVO.status).getText());
                postsDetailVO.setType(PostsType.valueOf(postsDetailVO.postsType).getText());
                initImgPath(postsDetailVO);
                userIds.add(postsDetailVO.userId);
                postsIds.add(postsDetailVO.id);
                return postsDetailVO;
            }).collect(Collectors.toList());
        }
        Map<Long, UserInfoVO> userInfoVOMap;
        Map<Long, PostsLike> postsLikeHashMap;
        if (!list.isEmpty()) {
            userInfoVOMap = userInfoService.listByIds(userIds);
            postsLikeHashMap = postsLikeService.listByUserId(userId, postsIds);
            Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
            Map<Long, PostsLike> finalPostsLikeHashMap = postsLikeHashMap;
            list.forEach(item -> {
                UserInfoVO userInfoVO = finalUserInfoVOMap.get(item.userId);
                UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
                BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
                userBasicInfoVO.setRole(Role.valueOf(userInfoVO.role).getText());
                item.setUserInfo(userBasicInfoVO);
                item.setFollow(false);
                if (followService.getDetail(userId, finalUserInfoVOMap.get(item.userId).getId()) != null) {
                    item.setFollow(true);
                }
                item.setLike(false);
                PostsLike postsLike = finalPostsLikeHashMap.get(item.id);
                if (postsLike != null) {
                    item.setLike(true);
                }
            });
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    private void setCommentAndLike(Posts item) {
//        List<Tags> list = tagsMapper.selectTagByPostId(item.getId());
//        Integer commentCount = PostCommentMapper.selectCount(new LambdaQueryWrapper<Comment>()
//                .eq(Comment::getArticleId, item.getId()));
        //获取点赞数量
        List<LikeCountDTO> list = postsLikeService.getLikeCountFromRedis();
        if (list != null && list.size() > 0) {
            Optional<LikeCountDTO> optionalLikeCount = list.stream()
                    .filter(likeCount -> likeCount.getPostId().equals(item.getId()))
                    .findFirst();
            optionalLikeCount.ifPresent(likeCount -> item.setLikeNum(likeCount.getCount()));

        }
//        item.setTagList(list);
//        item.setCommentCount(commentCount);
    }

    /**
     * 获取帖子详情
     *
     * @param id 帖子id
     * @return Result<PostsDetailVO>
     */
    @Override
    public Result<PostsDetailVO> getDetail(Long id) {
        Long userId = JwtUtil.getUserId();
        Posts posts = getById(id);
        if (posts == null) {
            return Result.failMsg("帖子不存在或已下架");
        }
        PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
        UserInfoVO userInfoVO = userInfoService.getUserInfoById(posts.getUserId());
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
        userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
        postsDetailVO.setUserInfo(userBasicInfoVO);
        initImgPath(postsDetailVO);
        postsDetailVO.setTags(tagsMapper.selectByPostId(postsDetailVO.id));
        postsDetailVO.setCategoryName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.categoryId)));
        postsDetailVO.setCollect(false);
        postsDetailVO.setFollow(false);
        if (userId != null && collectService.getDetail(userId, posts.getId()) != null) {
            postsDetailVO.setCollect(true);
        }
        if (userId != null && followService.getDetail(userId, posts.getUserId()) != null) {
            postsDetailVO.setFollow(true);
        }
        log.info(String.valueOf(postsDetailVO));
        return Result.OK(postsDetailVO);
    }

    /**
     * 更新浏览次数
     */
    @Override
    public Result<?> browse(Long userId, Long id) {
        Posts posts = getById(id);
        if (posts == null) {
            return Result.OK();
        }
        LocalDateTime now = LocalDateTime.now();
        if (userId == null) { // 未登录用户
            posts.setBrowseNum(posts.getBrowseNum() + 1);
            saveOrUpdate(posts);
            return Result.OK();
        } else { // 已登录用户
            // 检查是否存在浏览记录
            History existingHistory = historyService.findHistory(userId, id);
            if (existingHistory != null) {
                existingHistory.setUpdateTime(now); // 更新浏览时间
                historyService.updateHistory(existingHistory);
            } else {
                // 创建浏览记录对象
                History history = new History();
                history.setUserId(userId);
                history.setPostsId(id);
                history.setCreateTime(now); // 填充创建时间
                history.setUpdateTime(now); // 填充更新时间

                // 将浏览记录保存到history表
                historyService.saveHistory(history);
            }
            posts.setBrowseNum(posts.getBrowseNum() + 1);
            saveOrUpdate(posts);
        }
        return Result.OK();
    }

    /**
     * 拼接图片获取绝对路径
     */
    private void initImgPath(PostsDetailVO postsDetailVO) {
        postsDetailVO.setCoverPath(ossService.getOssImgPath(postsDetailVO.coverPath));
        if (StringUtils.isNotEmpty(postsDetailVO.imgPath)) {
            String[] arr = postsDetailVO.imgPath.split(",");
            List<String> list = Arrays.asList(arr);
            List<String> imgPathList = new ArrayList<>();
            list.forEach(item -> {
                imgPathList.add(ossService.getOssImgPath(item));
            });
            postsDetailVO.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }

    /**
     * 批量获取
     */
    @Override
    public Map<Long, PostsDetailVO> listByIds(List<Long> postsIds) {
        Map<Long, PostsDetailVO> postsHashMap = new HashMap<>();
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Posts::getId, postsIds);
        List<Posts> postsList = list(queryWrapper);
        postsList.forEach(item -> {
            PostsDetailVO postsDetailVO = BeanUtil.copyProperties(item, PostsDetailVO.class);
            this.initImgPath(postsDetailVO);
            postsHashMap.put(postsDetailVO.getId(), postsDetailVO);
        });
        return postsHashMap;
    }

    /**
     * 帖子修改
     */
    @Override
    public Result<?> update(PostsVO postsVO) {
        if (!Objects.equals(JwtUtil.getUserId(), postsVO.getUserId())) {
            return Result.fail("您无权操作!");
        }
        log.info("帖子id:" + postsVO.getId());
        log.info("修改前Posts:" + getById(postsVO.getId()));
        if (postsVO.getId() == null) {
            return Result.fail("修改失败，请重试!!!!");
        }
        if (StringUtils.isEmpty(postsVO.getTitle())) {
            return Result.fail("请输入标题");
        }
        if (StringUtils.isEmpty(postsVO.getContent())) {
            return Result.fail("请输入内容");
        }
        Posts posts = new Posts();
        BeanUtil.copyProperties(postsVO, posts);
        posts.setTitle(postsVO.getTitle());
        posts.setCategoryId(postsVO.getCategoryId());
        //dfa过滤
//            postsVO.setContent(HTMLUtils.deleteTag(postsVO.content));
//            postsVO.setDescription(HTMLUtils.deleteTag(postsVO.description));

        List<String> imgPathList = new ArrayList<>();
        Collections.addAll(imgPathList, postsService.getImgPathById(postsVO.getId()).split(","));
        log.info("修改前imgPathList:" + imgPathList);

        if (postsVO.getFiles() != null) {//上传新加的图片
            if ((postsVO.getFiles().length) > 9) {
                return Result.fail("最多可上传9张图片");
            }
            for (MultipartFile file : postsVO.getFiles()) {
                String imgPath;
                try {
                    imgPath = ossService.uploadFile(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                imgPathList.add(imgPath);
            }
        }
        String removeImgPath = postsVO.getRemoveFiles();
        if (removeImgPath != null) {
            List<String> removeUrls = new ArrayList<>(Arrays.asList(removeImgPath.split(",")));
            imgPathList.removeAll(removeUrls);
            removeUrls.replaceAll(url -> url.replace(ALIYUNOSS_PREFIX, ""));//把前缀删掉
            log.info(removeUrls.toString());
            for (String url : removeUrls) {
                ossService.delFile(url);
            }
            log.info("移除了" + removeUrls);
            log.info("移除完后的imgPathList:" + imgPathList);

        }
        log.info("修改后最终的imgpath:" + imgPathList);
        if (imgPathList.size() > 0) {
            posts.setCoverPath(imgPathList.get(0));
            posts.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            return Result.failMsg("请至少上传一张图片!");
        }

        saveOrUpdate(posts);
        BeanUtil.copyProperties(postsVO, getById(postsVO.getId()));
        return Result.OK("修改成功", getById(postsVO.getId()));
    }

    /**
     * 帖子搜索（MySQL LIKE 实现）
     *
     * @param condition 条件
     * @return List<Posts>
     */
    @Override
    public List<Posts> listPostsBySearch(ConditionVO condition) {
        String keywords = condition.getKeywords();
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Posts::getTitle, keywords)
                .or()
                .like(Posts::getContent, keywords)
                .orderByDesc(Posts::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 帖子删除
     *
     * @param userId 帖子所属用户id
     * @param id     帖子id
     * @return
     */
    @Override
    public Result<?> del(Long userId, Long id) {
        Posts post = getById(id);
        Assert.notNull(post, "帖子不存在或已删除");
        if (post.getUserId().equals(userId)) {
            post.setStatus(PostStatus.TRASHED.getValue());
            UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            update(post, updateWrapper);
            //更新帖子
            return Result.OK("删除成功");
        } else {
            return Result.fail("抱歉，您无权操作");
        }
    }

    @Override
    public Result<?> delete(Long userId, Long id) {
        Posts post = getById(id);
        Assert.notNull(post, "帖子不存在或已删除");
        if (post.getUserId().equals(userId)) {
            baseMapper.deleteById(id);
            //更新帖子
            return Result.OK("彻底删除成功");
        } else {
            return Result.fail("抱歉，您无权操作");
        }
    }

    @Override
    public Result<?> restore(Long userId, Long id) {
        Posts post = getById(id);
        Assert.notNull(post, "帖子不存在或已删除");
        if (post.getUserId().equals(userId)) {
            post.setStatus(PostStatus.PUBLISHED.getValue());
            UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            update(post, updateWrapper);
            //更新帖子
            return Result.OK("成功还原！");
        } else {
            return Result.fail("抱歉，您无权操作");
        }
    }

    /**
     * 根据帖子id获取img_path
     *
     * @param id 帖子id
     * @return String
     */
    @Override
    public String getImgPathById(Long id) {
        return postsMapper.getImgPathById(id);
    }

    /**
     * 根据postId更新点赞数
     *
     * @param postId     帖子ID
     * @param collectNum 点赞数
     */
    public void updatePostsCollectNum(Long postId, Integer collectNum) {
        // 创建更新条件
        UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", postId);
        // 创建更新内容
        Posts post = new Posts();
        post.setCollectNum(collectNum);
        // 执行更新操作
        update(post, updateWrapper);
    }


    @Override
    public Result<List<PostsDetailVO>> listHot() {
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("posts_type", 2)
                .eq("status", 3)
                .orderByDesc("(browse_num * 0.4 + collect_num * 0.2 + like_num * 0.3 + comment_num * 0.1)")
                .last("limit 10");
        List<Posts> posts = postsMapper.selectList(queryWrapper);
        List<PostsDetailVO> list = new ArrayList<>();
        for (Posts p : posts) {
            PostsDetailVO vo = BeanUtil.copyProperties(p, PostsDetailVO.class);
            // 填充用户信息
            UserInfoVO userInfo = userInfoService.getUserInfoAndFansById(p.userId).getData();
            if (userInfo != null) {
                UserBasicInfoVO basicInfo = new UserBasicInfoVO();
                BeanUtil.copyProperties(userInfo, basicInfo);
                vo.setUserInfo(basicInfo);
            }
            vo.setCategoryName(categoryService.getCategoryById(Long.valueOf(p.categoryId)));
            list.add(vo);
        }
        return Result.OK(list);
    }

    @Override
    public Result audit(PostsDTO postsDTO) {
        // 校验管理员权限
        Long userId = JwtUtil.getUserId();
        if (userId == null) {
            return Result.failMsg("请先登录");
        }
        UserInfoVO userInfo = userInfoService.getUserInfoAndFansById(userId).getData();
        if (userInfo == null || !Role.MANAGER.getValue().equals(userInfo.getRole())) {
            return Result.failMsg("无权限操作，仅管理员可审核");
        }
        Posts posts = getById(postsDTO.id);
        Assert.isTrue(posts != null, "帖子不存在！");
        postsMapper.audit(postsDTO);
        return Result.OK(posts);
    }
//    public void sendEmail(PostsDTO postsDTO){
//        Map<String, Object> map = new HashMap<>();
//        map.put("content", "尊敬的用户，您好!<br><br>" +
//                "您在校园墙<span style='font-weight:bold;'> </span>操作，本次请求的邮件验证码是：<br><br><span style='font-weight:bold; font-size:25px;'>" + code +
//                "</span><br><br>本验证码5分钟内有效，为了保证您账号的安全性，请及时输入。请不要告诉他人哦！");
//        EmailDTO emailDTO = EmailDTO.builder()
//                .email(email)
//                .subject("校园墙"+CodeType.getType(type)+CAPTCHA)
//                .template("common.html")
//                .commentMap(map)
//                .build();
//        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*",
//                new Message(JSON.toJSONBytes(emailDTO),
//                        new MessageProperties()));
//    }
}
