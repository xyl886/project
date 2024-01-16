package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.fileupload.FileUploadConfig;
import com.love.product.config.security.TokenConfig;
import com.love.product.entity.Posts;
import com.love.product.entity.UserAuth;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.EmailDTO;
import com.love.product.entity.req.UserPageReq;
import com.love.product.entity.vo.*;
import com.love.product.enums.CodeType;
import com.love.product.enums.Gender;
import com.love.product.enums.Role;
import com.love.product.enums.YesOrNo;
import com.love.product.mapper.PostsMapper;
import com.love.product.mapper.RoleMapper;
import com.love.product.mapper.UserInfoMapper;
import com.love.product.service.*;
import com.love.product.util.CaptchaUtils;
import com.love.product.util.CommonUtil;
import com.love.product.util.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.love.product.constant.CommonConstant.CAPTCHA;
import static com.love.product.constant.CommonConstant.EXPIRE_TIME;
import static com.love.product.constant.RabbitMQConstant.EMAIL_EXCHANGE;
import static com.love.product.constant.RedisKeyConstant.*;
import static com.love.product.entity.base.ResultCode.*;
import static com.love.product.util.CommonUtil.getRandomCode;


/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TokenConfig tokenConfig;
    @Resource
    private FileUploadConfig fileUploadConfig;
    @Resource
    private OssService ossService;
    @Resource
    private FollowService followService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisService redisService;
    @Resource
    private PostsMapper postsMapper;


    public void checkEmail(String email) {
        boolean matches = Pattern.compile("\\w+@{1}\\w+\\.{1}\\w+").matcher(email).matches();
        Assert.isTrue(matches, EMAIL_ERROR.getMsg());
    }

    private void checkCode(String key, String emailCode) {
        Object code = redisService.get(key);
        Assert.isTrue(code != null && code.equals(emailCode), ERROR_EXCEPTION_MOBILE_CODE.getMsg());
    }

    private void codeLimit(String email) {
        // 从缓存中查询邮箱最近一次发送验证码的时间戳
        Object lastSendTimestampObj = redisService.get(email);
        long lastSendTimestamp = 0L;
        if (lastSendTimestampObj != null) {
            lastSendTimestamp = Long.parseLong(lastSendTimestampObj.toString());
        }
        long currentTime = System.currentTimeMillis() / 1000; // 当前时间戳，单位为秒
        Assert.isTrue(
                currentTime - lastSendTimestamp > 60L,
                "发送验证码过于频繁，请稍后再试!");
        redisService.set(email, currentTime, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param type
     * @return
     */
    @Override
    public Result<?> sendCode(String email, String type) {
        codeLimit(email);
        checkEmail(email);
        String code = getRandomCode();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "尊敬的用户" + email + "，您好!<br><br>" +
                "您正在校园墙进行<span style='font-weight:bold;'>" + CodeType.getType(type) +
                "</span>操作，本次请求的邮件验证码是：<br><br><span style='font-weight:bold; font-size:25px;'>" + code +
                "</span><br><br>本验证码5分钟内有效，为了保证您账号的安全性，请及时输入。请不要告诉他人哦！");
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("校园墙" + CodeType.getType(type) + CAPTCHA)
                .template("common.html")
                .commentMap(map)
                .build();
        rabbitTemplate.convertAndSend(
                EMAIL_EXCHANGE,
                "*",
                new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        redisService.set(CODE + email, code, 5 * 60);
        return Result.OKMsg("验证码已发送，请查收！");
    }

    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserVO> login(LoginVO loginVO) {
        UserInfoVO userInfoVO = getByEmail(loginVO.email);
        if (userInfoVO == null) {
            return Result.failMsg("账号不存在!");
        }
        if (userInfoVO.getDeleted().equals(YesOrNo.YES.getValue())) {
            return Result.failMsg("登录失败，账号已注销");
        }
        if (userInfoVO.getStatus().equals(YesOrNo.YES.getValue())) {
            return Result.failMsg("登录失败，账号已禁用，请联系客服人员");
        }
        if (!loginVO.emailCode.isEmpty() && loginVO.password.isEmpty()) {
            checkCode("code:" + loginVO.email, loginVO.emailCode);
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(loginVO.password, userInfoVO.password);
            if (!matches) {
                return Result.failMsg("账号或密码错误");
            }
        }
        // 缓存用户信息到Redis，设置过期时间为7天，并删除验证码
        redisService.set(USER_USERINFO + userInfoVO.getId(), userInfoVO);
        redisService.expire(USER_USERINFO + userInfoVO.getId(), 7, TimeUnit.DAYS);
        redisService.del(CODE + loginVO.email);

        String accessToken = getOAuthToken(userInfoVO);
        if (accessToken == null) {
            return Result.failMsg("登录失败，请重试");
        }
        userInfoVO.setAccessToken(accessToken);
        userInfoVO.setEmail(loginVO.email);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfoVO,userVO);
        return Result.OK(userVO);
    }

    /**
     * 用户注册
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfoVO> userRegister(RegisterVO registerVO) {
        UserInfoVO userInfoVO = getByEmail(registerVO.email);
        if (userInfoVO != null) {
            return Result.failMsg("邮箱已注册，请修改！");
        }
        checkEmail(registerVO.email);
        if (registerVO.emailCode.isEmpty()) {
            return Result.failMsg("请输入验证码！");
        }
        if (registerVO.password.isEmpty()) {
            return Result.failMsg("请设置密码！");
        }
        checkCode("code:" + registerVO.email, registerVO.emailCode);

        if (!registerVO.password.equals(registerVO.confirmPassword)) {
            return Result.failMsg("两次输入密码不一致，请重新输入！");
        }
        LocalDateTime now = LocalDateTime.now();
        userInfoVO = new UserInfoVO();
        userInfoVO.setId(IdWorker.getId());
        userInfoVO.setEmail(registerVO.email);
        userInfoVO.setNickname(fileUploadConfig.getDefaultNickname());
        userInfoVO.setOriginalPassword(registerVO.password);
        userInfoVO.setPassword(new BCryptPasswordEncoder().encode(registerVO.password));
        userInfoVO.setAvatar(fileUploadConfig.getDefaultAvatar());
        userInfoVO.setGender(Gender.DUNNO.getValue());
        userInfoVO.setStatus(YesOrNo.NO.getValue());
        userInfoVO.setRole(Role.VISITOR.getValue());
        userInfoVO.setDeleted(YesOrNo.NO.getValue());
        userInfoVO.setCreateTime(now);
        userInfoVO.setUpdateTime(now);
        save(userInfoVO);
        return Result.OK(userInfoVO);
    }

    /**
     * 修改密码
     */
    @Override
    public Result<?> updatePwd(Long id, String currentPassword, String newPassword, String confirmPassword) {
        UserInfo userInfo = getById(id);
        if (userInfo == null) {
            return Result.failMsg("用户不存在！");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(currentPassword, userInfo.password);
        if (!matches) {
            return Result.failMsg("当前密码不正确！");
        }
        if (!newPassword.equals(confirmPassword)) {
            return Result.failMsg("新密码与确认密码不匹配！");
        }
        if (currentPassword.equals(newPassword)) {
            return Result.OKMsg("您的密码并未改动！");
        }
        if (!CommonUtil.isValidPassword(newPassword)) {
            return Result.failMsg("新密码不符合要求！");
        }
        userInfo.setOriginalPassword(newPassword);
        userInfo.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        saveOrUpdate(userInfo);
        return Result.OKMsg("修改成功！");
    }

    /**
     * 重置密码
     *
     * @param userId
     * @param resetVO
     * @return
     */

    @Override
    public Result<?> reset(Long userId, RegisterVO resetVO) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return Result.failMsg("用户异常，请联系管理员！");
        }
        if (!resetVO.password.equals(resetVO.confirmPassword)) {
            return Result.failMsg("新密码与确认密码不匹配！");
        }
        if (!CommonUtil.isValidPassword(resetVO.password)) {
            return Result.failMsg("新密码不符合要求！");
        }
        userInfo.setOriginalPassword(resetVO.password);
        userInfo.setPassword(new BCryptPasswordEncoder().encode(resetVO.password));
        saveOrUpdate(userInfo);
        return Result.OKMsg("重置成功！");
    }

    @Override
    public UserInfoVO getByEmail(String email) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getEmail, email).last("LIMIT 1");
        UserInfo userInfo = getOne(queryWrapper);
        return getUserInfoVO(userInfo);
    }

    /**
     * 获取用户详情
     */
    @Override
    public UserInfoVO getUserInfoById(Long id) {
        UserInfo userInfo = getById(id);
        return getUserInfoVO(userInfo);
    }

    private UserInfoVO getUserInfoVO(UserInfo userInfo) {
        UserInfoVO userInfoVO = null;
        if (userInfo != null) {
            userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo, userInfoVO);
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());
            userInfoVO.setFansNum(followService.getFansNumByUserId(userInfo.id));
            userInfoVO.setFollowNum(followService.getFollowNumByUserId(userInfo.id));
//            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
        }
        return userInfoVO;
    }

    /**
     * 调用OAuth2的获取令牌接口
     *
     * @description 1.将用户信息存入公共map中 2.获取访问令牌 3.写入"刷新令牌"到数据库
     */
    @Override
    public String getOAuthToken(UserInfoVO userInfoVO) {
        //获取OAuth2框架的配置信息，用于访问刷新令牌接口
        Map<String, String> tokenMap = tokenConfig.getConfig();
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("email", userInfoVO.email);
        mapParam.put("nickname", userInfoVO.nickname);
        mapParam.put("password", userInfoVO.originalPassword);
        mapParam.put("client_id", tokenMap.get("clientId"));
        mapParam.put("client_secret", tokenMap.get("secret"));
        mapParam.put("grant_type", tokenMap.get("grantTypes"));
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> mapResult = restTemplate
                    .getForObject(
                            fileUploadConfig.getHostIp() + "/oauth/token?username={email}&password={password}&client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}",
                            Map.class, mapParam);
            if (mapResult != null) {
                try {
                    setRefreshToken(userInfoVO.getId(), mapResult.get("refresh_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mapResult.get("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户的刷新令牌
     */
    @Override
    public void setRefreshToken(Long userId, String refreshToken) {
        if (refreshToken != null) {
            redisService.set(REFRESH_TOKEN + userId, refreshToken);
        }
    }

    /**
     * 用户列表
     */
    @Override
    public Map<Long, UserInfoVO> listByIds(List<Long> ids) {
        Map<Long, UserInfoVO> userInfoVOMap = new HashMap<>();
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserInfo::getId, ids);
        List<UserInfo> userInfos = list(queryWrapper);
        userInfos.forEach(item -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(item, userInfoVO);
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());
//            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
            userInfoVO.setFollowNum(followService.getFollowNumByUserId(userInfoVO.getId()));
            userInfoVO.setFansNum(followService.getFansNumByUserId(userInfoVO.getId()));
            userInfoVOMap.put(userInfoVO.getId(), userInfoVO);
        });
        return userInfoVOMap;
    }

    /**
     * 更新用户信息
     */
    @SneakyThrows
    @Override
    public Result<UserInfoVO> update(Long userId, String nickname, MultipartFile file, Integer gender, String hobby, String remark) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return Result.failMsg("用户不存在，请重试");
        }
        if (StringUtils.isEmpty(nickname)) {
            return Result.failMsg("请输入昵称");
        }
        Gender genderObj = Gender.valueOf(gender);
        if (genderObj == null) {
            return Result.failMsg("请选择性别");
        }
        userInfo.setNickname(nickname);
        userInfo.setGender(genderObj.getValue());
        userInfo.setHobby(hobby);
        userInfo.setRemark(remark);
        if (file != null) {
            String avatar = ossService.uploadFile(file);
            userInfo.setAvatar(avatar);
        }
        saveOrUpdate(userInfo);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO);
//            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
        userInfoVO.setGenderText(genderObj.getText());
        return Result.OK("保存成功", userInfoVO);
    }

    /**
     * 获取单个用户详情
     */
    @Override
    public Result<UserInfoVO> getUserInfoAndFansById(Long id) {
        UserInfo userInfo = getById(id);
        if (userInfo == null) {
            return Result.failMsg("用户不存在!");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO);
        Gender gender = Gender.valueOf(userInfoVO.getGender());
        userInfoVO.setGenderText(gender.getText());
//            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
        int followNum = followService.getFollowNumByUserId(userInfoVO.getId());
        userInfoVO.setFollowNum(followNum);
        int fansNum = followService.getFansNumByUserId(userInfoVO.getId());
        userInfoVO.setFansNum(fansNum);
        return Result.OK(userInfoVO);
    }

    @Override
    public ResultPage<UserVO> listUser(UserPageReq userPageReq) {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(userPageReq.roleId != null, UserInfo::getRole, userPageReq.roleId)
                .and(StringUtils.isNotBlank(userPageReq.getUsername()), wrapper ->
                        wrapper.like(UserInfo::getNickname, userPageReq.getUsername())
                                .or()
                                .like(UserInfo::getEmail, userPageReq.getUsername()))
                .orderByAsc(UserInfo::getCreateTime);
        Page<UserInfo> page = page(userPageReq.build(), lambdaQueryWrapper);
        List<UserVO> list = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(userInfo -> {
                UserVO userVO = BeanUtil.copyProperties(userInfo, UserVO.class);
                userVO.setRoleName(getRoleName(userVO.role));
                userVO.setUserStatus(userVO.status == 0 ? YesOrNo.NORMAL.getText() : YesOrNo.DISABLE.getText());
                userVO.setPostNum(postsMapper.selectList(
                        new LambdaQueryWrapper<Posts>().eq(Posts::getUserId, userVO.id)).size());
                userVO.setGenderText(Gender.valueOf(userVO.gender).getText());
//                userVO.setAvatar(fileUploadService.getImgPath(userVO.avatar));
                return userVO;
            }).collect(Collectors.toList());
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    public String getRoleName(Integer id) {
        UserAuth role = roleMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>().eq(UserAuth::getId, id));
        if (ObjectUtil.isNull(role)) {
            return Role.VISITOR.getText();
        } else {
            return role.roleName;
        }
    }

    @Override
    public Result insertUser(UserInfo user) {
        return null;
    }

    @Override
    public Result getUserById(Integer id) {
        return null;
    }

    @Override
    public Result updateUser(UserInfo user) {
        Assert.isTrue(!Objects.equals(JwtUtil.getUserId(), user.id), "无权限！");
        baseMapper.updateById(user);
        return Result.OK();
    }

    @Override
    public Result deleteBatch(List<Integer> ids) {
        return null;
    }

    @Override
    public Result getCurrentUserInfo() {
        return null;
    }

    @Override
    public Result getCurrentUserMenu() {
        return null;
    }

    @Override
    public Result updatePassword(Map<String, String> map) {
        return null;
    }

    @Override
    public Object getCaptcha(Captcha captcha) {
        /**
         * 获取验证码拼图（生成的抠图和带抠图阴影的大图及抠图坐标）
         **/
        //参数校验
        CaptchaUtils.checkCaptcha(captcha);
        //获取画布的宽高
        int canvasWidth = captcha.getCanvasWidth();
        int canvasHeight = captcha.getCanvasHeight();
        //获取阻塞块的宽高/半径
        int blockWidth = captcha.getBlockWidth();
        int blockHeight = captcha.getBlockHeight();
        int blockRadius = captcha.getBlockRadius();
        //获取资源图
        BufferedImage canvasImage = CaptchaUtils.getBufferedImage(captcha.getPlace());
        //调整原图到指定大小
        canvasImage = CaptchaUtils.imageResize(canvasImage, canvasWidth, canvasHeight);
        //随机生成阻塞块坐标
        int blockX = CaptchaUtils.getNonceByRange(blockWidth, canvasWidth - blockWidth - 10);
        int blockY = CaptchaUtils.getNonceByRange(10, canvasHeight - blockHeight + 1);
        //阻塞块
        BufferedImage blockImage = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_4BYTE_ABGR);
        //新建的图像根据轮廓图颜色赋值，源图生成遮罩
        CaptchaUtils.cutByTemplate(canvasImage, blockImage, blockWidth, blockHeight, blockRadius, blockX, blockY);
        // 移动横坐标
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        // 缓存
        saveImageCode(nonceStr, String.valueOf(blockX));
        //设置返回参数
        captcha.setNonceStr(nonceStr);
        captcha.setBlockY(blockY);
        captcha.setBlockSrc(CaptchaUtils.toBase64(blockImage, "png"));
        captcha.setCanvasSrc(CaptchaUtils.toBase64(canvasImage, "png"));
        return captcha;

    }

    /**
     * 缓存验证码，有效期15分钟
     *
     * @param key
     * @param code
     **/
    public void saveImageCode(String key, String code) {
        redisService.set(IMAGE_CODE + key, Long.parseLong(code), 15L, TimeUnit.MINUTES);
    }

    /**
     * 校验验证码
     *
     * @param imageKey
     * @param imageCode
     * @return boolean
     **/
    public String checkImageCode(String imageKey, String imageCode) {
        String text = (String) redisService.get(IMAGE_CODE + imageKey);
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            return "验证码已失效";
        }
        // 根据移动距离判断验证是否成功
        if (Math.abs(Integer.parseInt(text) - Integer.parseInt(imageCode)) > 3) {
            return "验证失败，请控制拼图对齐缺口";
        }
        return null;
    }
}
