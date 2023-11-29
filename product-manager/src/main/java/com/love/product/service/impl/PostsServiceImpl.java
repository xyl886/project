package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.consumer.PostsActionMessageConsumer;
import com.love.product.entity.History;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.Tags;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.PostEsDTO;
import com.love.product.entity.dto.PostEsHighlightData;
import com.love.product.entity.dto.PostsDTO;
import com.love.product.entity.dto.PostsSearchDTO;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.*;
import com.love.product.enumerate.PostStatus;
import com.love.product.enumerate.PostsType;
import com.love.product.enumerate.Role;
import com.love.product.mapper.PostsMapper;
import com.love.product.mapper.TagsMapper;
import com.love.product.service.*;
import com.love.product.strategy.context.SearchStrategyContext;
import com.love.product.util.HTMLUtils;
import com.love.product.util.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private TagsService tagsService;
    @Resource
    private TagsMapper tagsMapper;
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
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private SearchStrategyContext searchStrategyContext;
    @Resource
    private PostsActionMessageConsumer postsActionMessageConsumer;

    private final List<String> sensitiveWords = new ArrayList<>();

    private static final String SENSITIVE_WORD = "sensitive-words.txt";
    // 1. 定义审核规则

    private void loadSensitiveWords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SENSITIVE_WORD));
            String line;
            while ((line = reader.readLine()) != null) {
                sensitiveWords.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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
            loadSensitiveWords();
        for (String word : sensitiveWords) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }


    // 3. 编写定时任务或异步任务进行审核
    @Scheduled(fixedDelay = 10000) // 每60秒执行一次
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
                log.info("自动审核不通过:"+post.getId());
            } else {
                // 否则将其状态设置为 "已发布"
                post.setStatus(PostStatus.PUBLISHED.getValue());
                log.info("自动审核通过:"+post.getId());
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
        //dfa过滤
        postsVO.setTitle(HTMLUtils.deleteTag(postsVO.title));
        postsVO.setContent(HTMLUtils.deleteTag(postsVO.content));
        postsVO.setDescription(HTMLUtils.deleteTag(postsVO.description));
        postsVO.setTags(HTMLUtils.deleteTag(postsVO.getTags()));
        List<String> imgPathList = new ArrayList<>();
        if(postsType.equals(PostsType.LEAVE)){//闲置帖
            if(postsVO.getPrice() == null || postsVO.getPrice().doubleValue() <= 0){
                return Result.failMsg("请正确输入价格");
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
//              String imgPath = fileUploadService.uploadImage(multipartFile);      //本地存储
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
        //审核内容
//        if(discernSensitiveWords(postsVO.content,null)) {
//            posts.setStatus(PostStatus.PUBLISHED.getValue());
//        }else{
//            posts.setStatus(PostStatus.PENDING.getValue());
//        }
        posts.setStatus(PostStatus.PENDING.getValue());
        if(imgPathList.size() > 0){
            posts.setCoverPath(imgPathList.get(0));
            posts.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        //添加标签idList
        List<Long> tagIdList = getTagsList(postsVO);
        boolean flag = save(posts);
        log.info(tagIdList.toString());
        if (flag) {
            tagsMapper.savePostsTags(posts.id,tagIdList);
            return Result.OK("已提交，等待管理员审核！",posts);
        }
        return Result.failMsg("发布失败，请重试");
    }
    /**
     * 将数据库不存在的标签新增
     * @param postsVO
     * @return
     */
    private List<Long> getTagsList(PostsVO postsVO) {
        List<Long> tagIdList = new ArrayList<>();
        log.info(postsVO.getTags());
        List<String> tag =new ArrayList<>(Arrays.asList(postsVO.getTags().split(",")));
        tag.forEach(item ->{
            Tags tags = tagsMapper.selectOne(new QueryWrapper<Tags>().eq("tag_name", item));
            if (tags == null){
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
     * @param userId
     * @param postsPageReq
     * @return
     */
    @Override
    public ResultPage<PostsDetailVO> getPage(Long userId, PostsPageReq postsPageReq) {
        log.info("当前用户id,"+ userId);
        List<Long> followedUserIds = followService.getFollowedUserIdsByUserId(userId);
        log.info("已关注的用户id:"+followedUserIds);
        if (Objects.equals(postsPageReq.getCategoryId(),1) && followedUserIds.isEmpty()) {
            return ResultPage.OK(0, 1, 10, (Collection<PostsDetailVO>) null);
        }

        log.info(String.valueOf(postsPageReq));
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(postsPageReq.getPostsType()!=null,Posts::getPostsType, postsPageReq.getPostsType())//帖子类型不为空，则加入一个等于帖子类型的条件
                .eq(postsPageReq.getCategoryId()!=null&&postsPageReq.getCategoryId()!=1&&postsPageReq.getCategoryId()!=-1,
                        Posts::getSchool, postsPageReq.getCategoryId()) //非关注
                .inSql(postsPageReq.getTagId()!=null,Posts::getId,
                        "SELECT pt.posts_id FROM s_posts_tag pt " +
                                "WHERE pt.tag_id IN (SELECT id FROM s_tags WHERE deleted = 0 AND id = "
                        +postsPageReq.getTagId()+")")
                .eq(Objects.equals(postsPageReq.getCategoryId(),-1),Posts::getUserId, userId) //我的帖子
                .like(StringUtils.isNotBlank(postsPageReq.getTitle()),Posts::getTitle, postsPageReq.getTitle())//标题模糊查询
                .in(Objects.equals(postsPageReq.getCategoryId(), 1), Posts::getUserId, followedUserIds)  //关注帖
                .eq(postsPageReq.getStatus()!=null,Posts::getStatus, postsPageReq.getStatus())  //正常贴
                .orderByDesc(Posts::getCreateTime)
                .or().like(StringUtils.isNotBlank(postsPageReq.getTitle()),Posts::getDescription, postsPageReq.getTitle());
        log.info(String.valueOf(postsPageReq));
        Page<Posts> page = page(postsPageReq.build(), queryWrapper);

        List<PostsDetailVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(posts -> {
                PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
                postsDetailVO.setCategoryName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
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
                item.setFollow(false);
                if(followService.getDetail(userId, finalUserInfoVOMap.get(item.userId).getId()) != null){
                 item.setFollow(true);
                }
                item.setLike(false);
                PostsLike postsLike = finalPostsLikeHashMap.get(item.id);
                if(postsLike != null){
                    item.setLike(true);
                }
            });
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public ResultPage<PostsDetailVO> getPageByEs(PostsPageReq postQueryRequest) {
        // 获取查询数据
        Integer postsType = postQueryRequest.getPostsType();
        Long tagId = postQueryRequest.getTagId();
        Integer categoryId = postQueryRequest.getCategoryId();
        String title = postQueryRequest.getTitle();
        Integer status = postQueryRequest.getStatus();
        // es 起始页为 0
        long current = postQueryRequest.getCurrentPage() - 1;
        long pageSize = postQueryRequest.getPageSize();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("deleted", 0));
        boolQueryBuilder.filter(QueryBuilders.termQuery("status", status));
        if (postsType != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("posts_type", postsType));
        }
        if (tagId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("tag_id", tagId));
        }
        if (categoryId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("school", categoryId));
        }
        // 必须包含所有标签

        // 包含任何一个标签即可

        // 按关键词检索 满足其一√
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 搜索关键词高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("*").preTags("<font color='#eea6b7'>")
                .postTags("</font>");; //所有的字段都高亮
        highlightBuilder.requireFieldMatch(false);//如果要多个字段高亮,这项要为false

        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        sortBuilder.order(SortOrder.DESC);
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest)
                .withSort(sortBuilder)
                .build();
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
             log.info(searchHits.toString());
        Page<PostsDetailVO> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<PostsDetailVO> resourceList = new ArrayList<>();


        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<PostEsDTO>> searchHitList = searchHits.getSearchHits();
            // 搜索关键词高亮
            Map<Long, PostEsHighlightData> highlightDataMap = new HashMap<>();
            for (SearchHit hit: searchHits.getSearchHits()) {
                PostEsHighlightData data = new PostEsHighlightData();
                data.setId(Long.valueOf(Objects.requireNonNull(hit.getId())));
                if (hit.getHighlightFields().get("title") != null){
                    String highlightTitle = String.valueOf(hit.getHighlightFields().get("title"));
                    data.setTitle(highlightTitle.substring(1,highlightTitle.length()-1));
                    System.out.println(data.getTitle());
                }
                if (hit.getHighlightFields().get("content") != null){
                    String highlightContent = String.valueOf(hit.getHighlightFields().get("content"));
                    data.setContent(highlightContent.substring(1,highlightContent.length()-1));
                    System.out.println(data.getContent());
                }
                highlightDataMap.put(data.getId(),data);
            }
            // id列表
            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            // 根据id查找数据集
            List<Posts> postList = baseMapper.selectBatchIds(postIdList);
            if (postList != null) {
                Map<Long, List<Posts>> idPostMap = postList.stream().collect(Collectors.groupingBy(Posts::getId));
                postIdList.forEach(postId -> {
                    if (idPostMap.containsKey(postId)) {
                        // 搜索关键词高亮替换
                        Posts post = idPostMap.get(postId).get(0);
                        String hl_title = highlightDataMap.get(postId).getTitle();
                        String hl_content = highlightDataMap.get(postId).getContent();
                        if( hl_title != null && !hl_title.trim().equals("")){
                            post.setTitle(hl_title);
                        }
                        if (hl_content != null && !hl_content.trim().equals("")){
                            post.setContent(hl_content);
                        }
                        resourceList.add((PostsDetailVO) post);
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDTO.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        List<PostsDetailVO> list=new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords();
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
        if(posts == null){
            return Result.failMsg("帖子不存在或已下架");
        }
        PostsDetailVO postsDetailVO = BeanUtil.copyProperties(posts, PostsDetailVO.class);
        UserInfoVO userInfoVO = userInfoService.getUserInfoById(posts.getUserId());
        UserBasicInfoVO userBasicInfoVO=new UserBasicInfoVO();
        BeanUtil.copyProperties(userInfoVO, userBasicInfoVO);
        userBasicInfoVO.setRole(Role.valueOf(userInfoVO.getRole()).getText());
        postsDetailVO.setUserInfo(userBasicInfoVO);
        initImgPath(postsDetailVO);
        postsDetailVO.setTags(tagsMapper.selectByPostId(postsDetailVO.id));
        postsDetailVO.setCategoryName(categoryService.getCategoryById(Long.valueOf(postsDetailVO.school)));
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
        if(posts == null) {
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
        if(postsVO.getId() == null){
            return Result.fail("修改失败，请重试!!!!");
        }
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
            //dfa过滤
            postsVO.setContent(HTMLUtils.deleteTag(postsVO.content));
            postsVO.setDescription(HTMLUtils.deleteTag(postsVO.description));

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
        Assert.notNull(post,"帖子不存在或已删除");
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
    public Result<?> delete(Long userId,Long id) {
        Posts post = getById(id);
        Assert.notNull(post,"帖子不存在或已删除");
        if (post.getUserId().equals(userId)) {
            baseMapper.deleteById(id);
            //更新帖子
            return Result.OK("彻底删除成功");
        } else {
            return Result.fail("抱歉，您无权操作");
        }
    }
    @Override
    public Result<?> restore(Long userId,Long id) {
        Posts post = getById(id);
        Assert.notNull(post,"帖子不存在或已删除");
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
     * @param id 帖子id
     * @return String
     */
    @Override
    public String getImgPathById(Long id) {
        return postsMapper.getImgPathById(id);
    }

    /**
     * 根据postId更新点赞数
     * @param postId 帖子ID
     * @param collectNum 点赞数
     */
    public void updatePostsCollectNum(Long postId, Integer collectNum){
        // 创建更新条件
        UpdateWrapper<Posts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", postId);
        // 创建更新内容
        Posts post = new Posts();
        post.setCollectNum(collectNum);
        // 执行更新操作
        update(post, updateWrapper);
    }
//    @Override
//    public Result<List<Map<Long, String>>> listHot() {
//        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("posts_type", 2)
//                .eq("status", 3)
//                .orderByDesc("(browse_num * 0.4 + collect_num * 0.2 + like_num * 0.3 + comment_num * 0.1)")
//                .last("limit 10");
//        List<Posts> posts = postsMapper.selectList(queryWrapper);
//        List<Map<Long, String>> resultList = new ArrayList<>();
//        for (Posts post : posts) {
//            Map<Long, String> map = new HashMap<>();
//            map.put(post.getId(), post.getTitle());
//            resultList.add(map);
//        }
//        return Result.OK(resultList);
//    }
    @Override
    public Result<List<Posts>> listHot() {
        QueryWrapper<Posts> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("posts_type", 2)
                .eq("status", 3)
                .orderByDesc("(browse_num * 0.4 + collect_num * 0.2 + like_num * 0.3 + comment_num * 0.1)")
                .last("limit 10");
         List<Posts> posts= postsMapper.selectList(queryWrapper);
        return Result.OK(posts);
    }

    @Override
    public Result audit(PostsDTO postsDTO) {
     Posts posts=getById(postsDTO.id);
     Assert.isTrue(posts!=null,"帖子不存在！");
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
