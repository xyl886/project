package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.fileupload.AliyunOSS;
import com.love.product.entity.History;
import com.love.product.entity.Posts;
import com.love.product.entity.PostsLike;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.req.PostsReq;
import com.love.product.entity.vo.PostsVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.PostsType;
import com.love.product.enumerate.School;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.PostsMapper;
import com.love.product.service.*;
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

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 */
@Slf4j
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements PostsService {


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
    /**
     * 发布帖子
     */
    @Override
    public Result<Posts> add(Long userId, PostsReq postsReq) throws IOException {
        PostsType postsType = PostsType.valueOf(postsReq.getPostsType());
        School school = School.valueOf(postsReq.getSchool());
        if(postsType == null){
            return Result.failMsg("请选择帖子类型");
        }
        if(StringUtils.isBlank(postsReq.getTitle())){
            return Result.failMsg("标题不能为空");
        }
        if(StringUtils.isBlank(postsReq.getContent())){
            return Result.failMsg("内容不能为空");
        }
        if(school == null){
            return Result.failMsg("请选择校区");
        }
        List<String> imgPathList = new ArrayList<>();
        if(postsType.equals(PostsType.LEAVE)){//闲置帖
            if(postsReq.getPrice() == null || postsReq.getPrice().doubleValue() <= 0){
                return Result.failMsg("请输入价格");
            }
            if(postsReq.getFiles() == null || postsReq.getFiles().length == 0){
                return Result.failMsg("请至少上传一张图片");
            }
        }
        if(postsReq.getFiles() != null){
            if(postsReq.getFiles().length > 9){
                return Result.failMsg("最多可上传9张图片");
            }
            //上传图片
            for(MultipartFile multipartFile : postsReq.getFiles()){
//            String imgPath = fileUploadService.uploadImage(multipartFile);      //本地存储
                String imgPath =  ossService.uploadFile(multipartFile);        //oss对象存储
                imgPathList.add(imgPath);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Posts posts = new Posts();
        BeanUtil.copyProperties(postsReq,posts);
        posts.setId(IdWorker.getId());
        posts.setUserId(userId);
        posts.setCreateTime(now);
        posts.setUpdateTime(now);
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
     */
    @Override
    public ResultPage<PostsVO> getPage(Long userId,PostsPageReq postsPageReq) {
        if(userId == null && (Objects.equals(postsPageReq.getSchool(),3) ||Objects.equals(postsPageReq.getSchool(),4))){  //未登录
            return ResultPage.FAIL(403,"请登录");
        }
        List<Long> followedUserIds = followService.getFollowedUserIdsByUserId(userId);
        log.info("已关注的用户id:"+followedUserIds);
        if (Objects.equals(postsPageReq.getSchool(),4) && followedUserIds.isEmpty()) {
            return ResultPage.OK(0, 1, 10, (Collection<PostsVO>) null);
        }
//        如果查询参数postsPageReq的帖子类型不为空，则加入一个等于帖子类型的条件。
//        如果查询参数postsPageReq的学校不为空且不等于3，则加入一个等于学校的条件。
//        如果查询参数postsPageReq的学校等于3，则加入一个等于userId的条件。
//        加入一个等于帖子状态的条件。
//        按照帖子创建时间降序排序。
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(postsPageReq.getPostsType()!=null,Posts::getPostsType, postsPageReq.getPostsType())
        .eq(postsPageReq.getSchool()!=null&&postsPageReq.getSchool()!=3&&postsPageReq.getSchool()!=4,Posts::getSchool, postsPageReq.getSchool())
        .eq(Objects.equals(postsPageReq.getSchool(),3),Posts::getUserId, userId)
        .in(Objects.equals(postsPageReq.getSchool(), 4), Posts::getUserId, followedUserIds)
        .eq(Posts::getStatus, YesOrNo.NO.getValue())
        .orderByDesc(Posts::getCreateTime);
        Page<Posts> page = page(postsPageReq.build(), queryWrapper);
        List<PostsVO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> postsIds = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(posts -> {
                PostsVO postsVO = BeanUtil.copyProperties(posts, PostsVO.class);
                School school = School.valueOf(postsVO.getSchool());
                postsVO.setSchoolName(school!=null?school.getText():"");
                initImgPath(postsVO);
                userIds.add(postsVO.getUserId());
                postsIds.add(postsVO.getId());
                return postsVO;
            }).collect(Collectors.toList());
        }
        Map<Long, UserInfoVO> userInfoVOMap;
        Map<Long, PostsLike> postsLikeHashMap;
        if(Objects.equals(postsPageReq.getPostsType(),PostsType.SCHOOL.getValue()) && list.size() > 0){
            userInfoVOMap = userInfoService.listByIds(userIds);
            postsLikeHashMap = postsLikeService.listByUserId(userId,postsIds);
            Map<Long, UserInfoVO> finalUserInfoVOMap = userInfoVOMap;
            Map<Long, PostsLike> finalPostsLikeHashMap = postsLikeHashMap;
            list.forEach(item -> {
                UserInfoVO userInfoVO = finalUserInfoVOMap.get(item.getUserId());
                item.setUserInfo(userInfoVO);
                item.setLike(false);
                PostsLike postsLike = finalPostsLikeHashMap.get(item.getId());
                if(postsLike != null){
                    item.setLike(true);
                }
            });
        }
        log.info(String.valueOf(ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list)));
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 详情
     *
     */
    @Override
    public Result<PostsVO> getDetail(Long userId,Long id) {
        Posts posts = getById(id);
        if(posts == null || posts.getStatus().equals(YesOrNo.YES.getValue())){
            return Result.failMsg("帖子不存在或已下架");
        }
        PostsVO postsVO = BeanUtil.copyProperties(posts, PostsVO.class);
        School school = School.valueOf(postsVO.getSchool());
        postsVO.setSchoolName(school!=null?school.getText():"");
        UserInfoVO userInfoVO = userInfoService.getUserInfoById(posts.getUserId());
        postsVO.setUserInfo(userInfoVO);
        initImgPath(postsVO);
        postsVO.setCollect(false);
        postsVO.setFollow(false);
        if(userId != null && collectService.getDetail(userId,posts.getId()) != null){
            postsVO.setCollect(true);
        }
        if(userId != null && followService.getDetail(userId,posts.getUserId()) != null){
            postsVO.setFollow(true);
        }
        log.info(String.valueOf(postsVO));
        return Result.OK(postsVO);
    }

    /**
     * 更新浏览次数
     */
    @Override
    public Result<?> browse(Long userId,Long id) {
        Posts posts = getById(id);
        if(posts != null) {
            LocalDateTime now = LocalDateTime.now();
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
    private void initImgPath(PostsVO postsVO){
        postsVO.setCoverPath(ossService.getOssImgPath(postsVO.getCoverPath()));//todo OSS存储
        if(StringUtils.isNotEmpty(postsVO.getImgPath())){
            String[] arr = postsVO.getImgPath().split(",");
            List<String> list = Arrays.asList(arr);
            List<String> imgPathList = new ArrayList<>();
            list.forEach(item-> {
                imgPathList.add(ossService.getOssImgPath(item));//todo OSS存储
            });
            postsVO.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }

    /**
     * 批量获取
     */
    @Override
    public Map<Long, PostsVO> listByIds(List<Long> postsIds){
        Map<Long, PostsVO> postsHashMap = new HashMap<>();
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Posts::getId,postsIds);
        List<Posts> postsList = list(queryWrapper);
        postsList.forEach(item -> {
            PostsVO postsVO = BeanUtil.copyProperties(item, PostsVO.class);
            initImgPath(postsVO);
            postsHashMap.put(postsVO.getId(), postsVO);
        });
        return postsHashMap;
    }
    /**
     * 帖子修改
     */
    @SneakyThrows
    @Override
    public Result<?> update(Long id, PostsReq postsReq, String title, String content, String school){
//        System.out.println("帖子id:"+id);
//        System.out.println("修改前Posts:"+getById(id));
        if(id != null){
            if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(postsReq.getTitle())){
                return Result.failMsg("请输入标题");
            }
            if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(postsReq.getContent())){
                return Result.failMsg("请输入内容");
            }
            School schoolObj = School.valueOf(postsReq.getSchool());
            Posts posts = new Posts();
            BeanUtil.copyProperties(postsReq,posts);
            posts.setTitle(postsReq.getTitle());
            posts.setSchool(schoolObj.getValue());
            posts.setContent(postsReq.getContent());

            if(postsReq.getFiles() == null || postsReq.getFiles().length == 0){
                return Result.failMsg("请至少上传一张图片");
            }
            if(postsReq.getFiles() != null){
                if(postsReq.getFiles().length > 9){
                    return Result.failMsg("最多可上传9张图片");
                }
                List<String> imgPathList = new ArrayList<>();
                for(MultipartFile multipartFile : postsReq.getFiles()){
                    String imgPath = ossService.uploadFile(multipartFile);//上传图片 todo oss存储
                    imgPathList.add(imgPath);
                }
                if(imgPathList.size() > 0){
                    posts.setCoverPath(imgPathList.get(0));
                    posts.setImgPath(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
            }
            saveOrUpdate(posts);
            BeanUtil.copyProperties(postsReq,getById(id));
            return Result.OK("修改成功",getById(id));
        }
        return Result.failMsg("修改失败，请重试");
    }
    /**
     * 帖子删除
     */
    @Override
    public Result<?> del(Long userId,Long id) {
        Posts post = getById(id);
        if(post != null){
            if(post.getUserId().equals(userId)){
                String filelist = post.getImgPath();
                String[] imageUrls = filelist.split(",");
                List<String> imageUrlList = Arrays.asList(imageUrls);
                log.info("图片："+imageUrlList);
                for (String file : imageUrlList) {
                    try {
                        ossService.delFile(file.replace("https://" + AliyunOSS.BUCKET_NAME + "." + AliyunOSS.END_POINT + "/",""));
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
                return Result.failMsg("抱歉，您无权操作");
            }
        }else{
            return Result.failMsg("帖子不存在或已删除");
        }
    }
}
