package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Category;
import com.love.product.entity.History;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.PostsSearchDTO;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.*;
import com.love.product.enumerate.*;
import com.love.product.mapper.PostsMapper;
import com.love.product.service.*;
import com.love.product.strategy.context.SearchStrategyContext;
import com.love.product.util.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
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
    private  PostsMapper postsMapper;
    @Resource
    private TagService tagService;
    @Resource
    private HistoryService historyService;

    @Resource
    private OssService ossService;

    @Resource
    private FileUploadService fileUploadService;

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
    private  CategoryService categoryService;

    @Resource
    private SearchStrategyContext searchStrategyContext;
    // 1. 定义审核规则
    private static final List<String> sensitiveWords = Arrays.asList("敏感词1", "敏感词2");

    // 2. 在帖子创建或修改时进行自动审核
    public void createOrUpdatePost(Posts post) {
        boolean containsSensitiveWords = post.getTitle() != null && containsSensitiveWords(post.getTitle());
        if (post.getContent() != null && containsSensitiveWords(post.getContent())) {
            containsSensitiveWords = true;
        }
        if (containsSensitiveWords) {
            // 如果帖子包含敏感词，则将其状态设置为 "待审核"
            post.setStatus(PostStatus.PENDING.getValue());
        } else {
            // 否则将其状态设置为 "已发布"
            post.setStatus(PostStatus.PUBLISHED.getValue());
        }
        // 保存帖子到数据库中
        postsMapper.insert(post);
    }

    // 判断文本中是否包含敏感词
    private boolean containsSensitiveWords(String text) {
        for (String word : sensitiveWords) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    // 3. 编写定时任务或异步任务进行审核
    @Scheduled(fixedDelay = 60000) // 每60秒执行一次
    public void autoReviewPosts() {
        // 查询所有状态为 "待审核" 的帖子
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", PostStatus.PENDING.getValue());
        List<Posts> pendingPosts = postsMapper.selectList(queryWrapper);

        // 对每个帖子进行审核
        for (Posts post : pendingPosts) {
            if (containsSensitiveWords(post.getTitle()) || containsSensitiveWords(post.getContent())) {
                // 如果帖子仍然包含敏感词，则将其状态设置为 "审核不通过"
                post.setStatus(PostStatus.REJECTED.getValue());
            } else {
                // 否则将其状态设置为 "已发布"
                post.setStatus(PostStatus.PUBLISHED.getValue());
            }
            // 更新帖子状态到数据库中
            postsMapper.updateById(post);
        }
    }



    /**
     * 发布帖子
     */
    @SneakyThrows
    @Override
    public Result<Posts> add(Long userId, PostsVO postsVO) {
        PostsType postsType = PostsType.valueOf(postsVO.getPostsType());
//        if(postsType == null){
//            return Result.failMsg("请选择帖子类型");
//        }
//        if(StringUtils.isBlank(postsVO.getTitle())){
//            return Result.failMsg("标题不能为空");
//        }
//        if(StringUtils.isBlank(postsVO.getContent())){
//            return Result.failMsg("内容不能为空");
//        }
//        if(School.valueOf(postsVO.getSchool()) == null){
//            return Result.failMsg("请选择标签");
//        }
        List<String> imgPathList = new ArrayList<>();
        if(postsType.equals(PostsType.LEAVE)){//闲置帖
            if(postsVO.getPrice() == null || postsVO.getPrice().doubleValue() <= 0){
                return Result.failMsg("请输入价格");
            }
            if(postsVO.getFiles() == null || postsVO.getFiles().length == 0){
                return Result.failMsg("请至少上传一张图片");
            }
        }
        if(postsVO.getFiles() != null){
            if(postsVO.getFiles().length > 9){
                return Result.failMsg("最多可上传9张图片");
            }
            //上传图片
            for(MultipartFile multipartFile : postsVO.getFiles()){
//            String imgPath = fileUploadService.uploadImage(multipartFile);      //本地存储
                String imgPath =  ossService.uploadFile(multipartFile);        //oss对象存储
                imgPathList.add(imgPath);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Posts posts = new Posts();
        BeanUtil.copyProperties(postsVO,posts);
        posts.setId(IdWorker.getId());
        posts.setUserId(userId);
        posts.setCreateTime(now);
        posts.setUpdateTime(now);
        posts.setStatus(PostStatus.PUBLISHED.getValue());
        if(imgPathList.size() > 0){
            posts.setCoverPath(imgPathList.get(0));
            posts.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        boolean flag = save(posts);
        if(flag){
            return Result.OK("发布成功",posts);
        }
        return Result.failMsg("发布失败，请重试");
    }

    /**
     * 分页
     * @param userId
     * @param postsPageReq
     * @return
     */
    @Override
    public ResultPage<PostsDetailVO> getPage(Long userId, PostsPageReq postsPageReq) {

        List<Long> followedUserIds = followService.getFollowedUserIdsByUserId(userId);
        log.info("已关注的用户id:"+followedUserIds);
        if (Objects.equals(postsPageReq.getSchool(),1) && followedUserIds.isEmpty()) {
            return ResultPage.OK(0, 1, 10, (Collection<PostsDetailVO>) null);
        }

        log.info(String.valueOf(postsPageReq));
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(postsPageReq.getPostsType()!=null,Posts::getPostsType, postsPageReq.getPostsType())//帖子类型不为空，则加入一个等于帖子类型的条件
                .eq(postsPageReq.getSchool()!=null&&postsPageReq.getSchool()!=1&&postsPageReq.getSchool()!=-1, Posts::getSchool, postsPageReq.getSchool()) //非关注
                .eq(Objects.equals(postsPageReq.getSchool(),-1),Posts::getUserId, userId) //我的帖子
                .like(StringUtils.isNotBlank(postsPageReq.getTitle()),Posts::getTitle, postsPageReq.getTitle())  //标题模糊查询
                .in(Objects.equals(postsPageReq.getSchool(), 1), Posts::getUserId, followedUserIds)  //关注帖
                .eq(postsPageReq.getStatus()!=null,Posts::getStatus, postsPageReq.getStatus())  //正常贴
                .orderByDesc(Posts::getCreateTime);
        log.info(String.valueOf(postsPageReq));
        Page<Posts> page = page(postsPageReq.build(), queryWrapper);

        List<PostsDetailVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(posts -> {
                PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
                postsDetailVO.setSchoolName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
                initImgPath(postsDetailVO);
                userIds.add(postsDetailVO.userId);
                postsIds.add(postsDetailVO.id);
                return postsDetailVO;
            }).collect(Collectors.toList());
        }
        Map<Long, UserInfoVO> userInfoVOMap;
        Map<Long, PostsLike> postsLikeHashMap;
        if(list.size() > 0){
            userInfoVOMap = userInfoService.listByIds(userIds);
            postsLikeHashMap = postsLikeService.listByUserId(userId,postsIds);
            Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
            Map<Long, PostsLike> finalPostsLikeHashMap = postsLikeHashMap;
            list.forEach(item -> {
                UserInfoVO userInfoVO = finalUserInfoVOMap.get(item.userId);
                UserBasicInfoVO userBasicInfoVO=new UserBasicInfoVO();
                BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
                userBasicInfoVO.setRole(Role.valueOf(userInfoVO.role).getText());
                item.setUserInfo(userBasicInfoVO);
                item.setLike(false);
                PostsLike postsLike = finalPostsLikeHashMap.get(item.id);
                if(postsLike != null){
                    item.setLike(true);
                }
            });
        }

        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 获取帖子详情
     * @param userId 用户id
     * @param id  帖子id
     * @return Result<PostsDetailVO>
     */
    @Override
    public Result<PostsDetailVO> getDetail(Long userId, Long id) {
        Posts posts = getById(id);
        if(posts == null || posts.getStatus().equals(YesOrNo.YES.getValue())){
            return Result.failMsg("帖子不存在或已下架");
        }
        PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
        UserInfoVO userInfoVO = userInfoService.getUserInfoById(posts.getUserId());
        UserBasicInfoVO userBasicInfoVO=new UserBasicInfoVO();
        BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
        userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
        postsDetailVO.setUserInfo(userBasicInfoVO);
        initImgPath(postsDetailVO);
        postsDetailVO.setSchoolName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
        postsDetailVO.setCollect(false);
        postsDetailVO.setFollow(false);
        if(userId != null && collectService.getDetail(userId,posts.getId()) != null){
            postsDetailVO.setCollect(true);
        }
        if(userId != null && followService.getDetail(userId,posts.getUserId()) != null){
            postsDetailVO.setFollow(true);
        }
        log.info(String.valueOf(postsDetailVO));
        return Result.OK(postsDetailVO);
    }

    /**
     * 更新浏览次数
     */
    @Override
    public Result<?> browse(Long userId,Long id) {
        Posts posts = getById(id);
        if(posts != null) {
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
        }
        return Result.OK();
    }

    /**
     * 拼接图片获取绝对路径
     */
    private void initImgPath(PostsDetailVO postsDetailVO){
        postsDetailVO.setCoverPath(ossService.getOssImgPath(postsDetailVO.coverPath));//todo OSS存储
        if(StringUtils.isNotEmpty(postsDetailVO.imgPath)){
            String[] arr = postsDetailVO.imgPath.split(",");
            List<String> list = Arrays.asList(arr);
            List<String> imgPathList = new ArrayList<>();
            list.forEach(item-> {
                imgPathList.add(ossService.getOssImgPath(item));//todo OSS存储
            });
            postsDetailVO.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }

    /**
     * 批量获取
     */
    @Override
    public Map<Long, PostsDetailVO> listByIds(List<Long> postsIds){
        Map<Long, PostsDetailVO> postsHashMap = new HashMap<>();
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Posts::getId,postsIds);
        List<Posts> postsList = list(queryWrapper);
        postsList.forEach(item -> {
            PostsDetailVO postsDetailVO = BeanUtil.copyProperties(item, PostsDetailVO.class);
            initImgPath(postsDetailVO);
            postsHashMap.put(postsDetailVO.getId(), postsDetailVO);
        });
        return postsHashMap;
    }
    /**
     * 帖子修改
     */
    @Override
    public Result<?> update(PostsVO postsVO){
        if(!Objects.equals(JwtUtil.getUserId(), postsVO.getUserId())){
            return Result.fail("您无权操作!");
        }
        log.info("帖子id:"+ postsVO.getId());
        log.info("修改前Posts:"+getById(postsVO.getId()));
        if(postsVO.getId() != null){
            if(StringUtils.isEmpty(postsVO.getTitle())){
                return Result.fail("请输入标题");
            }
            if(StringUtils.isEmpty(postsVO.getContent())){
                return Result.fail("请输入内容");
            }
            Posts posts = new Posts();
            BeanUtil.copyProperties(postsVO,posts);
            posts.setTitle(postsVO.getTitle());
            posts.setSchool(postsVO.getSchool());
            posts.setContent(postsVO.getContent());

            List<String> imgPathList = new ArrayList<>();
            Collections.addAll(imgPathList, postsService.getImgPathById(postsVO.getId()).split(","));
            log.info("修改前imgPathList:" + imgPathList);

            if(postsVO.getFiles() != null) {//上传新加的图片
                if ((postsVO.getFiles().length) > 9) {
                    return Result.fail("最多可上传9张图片");
                }
                for (MultipartFile file : postsVO.getFiles()) {
                    String imgPath;// todo oss存储
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
            BeanUtil.copyProperties(postsVO,getById(postsVO.getId()));
            return Result.OK("修改成功",getById(postsVO.getId()));
        }
        return Result.fail("修改失败，请重试!!!!");
    }

    /**
     * 帖子搜索
     * @param condition 条件
     * @return List<PostsSearchDTO>
     */
    @Override
    public List<PostsSearchDTO> listPostsBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    /**
     * 帖子删除
     * @param userId 帖子所属用户id
     * @param id 帖子id
     * @return
     */
    @Override
    public Result<?> del(Long userId,Long id) {
        Posts post = getById(id);
        if(post != null){
            if(post.getUserId().equals(userId)){
                String[] imageUrls = post.getImgPath().split(",");
                List<String> imageUrlList = Arrays.asList(imageUrls);
                log.info("图片："+imageUrlList);
                for (String file : imageUrlList) {
                    try {
                        ossService.delFile(file.replace(ALIYUNOSS_PREFIX,""));
                        log.info("成功删除图片：" + file);
                    } catch (Exception e) {
                        log.error("删除图片失败：" + file, e);
                    }
                }
                postsService.removeById(id);
                Posts posts = postsService.getById(post.getId());
                //更新帖子
                postsService.saveOrUpdate(posts);
                return Result.OK("删除成功",post);
            }else{
                return Result.fail("抱歉，您无权操作");
            }
        }else{
            return Result.fail("帖子不存在或已删除");
        }
    }

    /**
     * 根据帖子id获取img_path
     * @param id 帖子id
     * @return String
     */
    @Override
    public String getImgPathById(Long id) {
        return postsMapper.getImgPathById(id);
    }

    /**
     * 更新点赞和收藏数
     * @param posts
     */
    public void updatePostsCollectNum(Posts posts){
        saveOrUpdate(posts);
    }

    @Override
    public Result<Posts> listHot() {
        return null;
    }
}
