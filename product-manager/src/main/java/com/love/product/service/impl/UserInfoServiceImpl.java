package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.Exception.BizException;
import com.love.product.config.fileupload.FileUploadConfig;
import com.love.product.config.security.TokenConfig;
import com.love.product.constant.CacheConstant;
import com.love.product.constant.CodeConstant;
import com.love.product.constant.CommonConstant;
import com.love.product.model.DTO.EmailDTO;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.model.VO.UserInfoVO;
import com.love.product.enumerate.Gender;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.UserInfoMapper;
import com.love.product.service.FileUploadService;
import com.love.product.service.FollowService;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.CommonUtil;
import com.love.product.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.love.product.constant.CommonConstant.CAPTCHA;
import static com.love.product.constant.CommonConstant.EXPIRE_TIME;
import static com.love.product.constant.RabbitMQConstant.EMAIL_EXCHANGE;
import static com.love.product.util.CommonUtil.checkEmail;
import static com.love.product.util.CommonUtil.getRandomCode;


/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Value("${server.port}")
    private String port;
    @Resource
    private EmailUtil emailUtil;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TokenConfig tokenConfig;
    @Resource
    private FileUploadConfig fileUploadConfig;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private FollowService followService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisService redisService;

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */

    @Override
    public Result<?> sendCode(String email) {
        // 从缓存中查询邮箱最近一次发送验证码的时间戳
//        Long lastSendTimestamp = CodeConstant.CACHE.get(email);
        Object lastSendTimestampObj = redisService.get(email);
        Long lastSendTimestamp = null;
        if (lastSendTimestampObj != null) {
            lastSendTimestamp = Long.parseLong(lastSendTimestampObj.toString());
        }
        long currentTime = System.currentTimeMillis() / 1000; // 当前时间戳，单位为秒
        if (lastSendTimestamp != null && currentTime - lastSendTimestamp < EXPIRE_TIME) {
            return Result.failMsg("发送验证码过于频繁，请稍后再试!");
        }
        CodeConstant.CACHE.put(email, currentTime);
        redisService.set(email, currentTime, EXPIRE_TIME, TimeUnit.SECONDS);
        if (!checkEmail(email)) {
            return Result.failMsg("请输入正确邮箱");
        }
        String code = getRandomCode();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "您的验证码为 " + code + " 有效期5分钟，请不要告诉他人哦！");
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject(CAPTCHA)
                .template("common.html")
                .commentMap(map)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        redisService.set("code:" + email, code, 5 * 60);
        return Result.OKMsg("验证码已发送，请查收！");
    }

    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfoVO> login(String email, String password,String emailCode){
        UserInfoVO userInfoVO = getByEmail(email);
        if(userInfoVO != null){
            if(userInfoVO.getDeleted().equals(YesOrNo.YES.getValue())){
                return Result.failMsg("登录失败，账号已注销");
            }
            if(userInfoVO.getStatus().equals(YesOrNo.YES.getValue())){
                return Result.failMsg("登录失败，账号已禁用，请联系客服人员");
            }
            if(!emailCode.isEmpty()&&password.isEmpty()){
                // 从 Redis 中获取验证码
                String redisCode = (String) redisService.get("code:" + email);
                log.info(redisCode);
                if (redisCode == null) {
                    return Result.failMsg("验证码已过期，请重新获取");
                }
                // 比较验证码是否正确
                if(!redisCode.equals(emailCode)) {
                    return Result.failMsg("验证码错误，请重新输入");
                }
            }else {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                boolean matches = encoder.matches(password, userInfoVO.getPassword());
                if (!matches) {
                    return Result.failMsg("账号或密码错误");
                }
            }
            //一定要在获取token前缓存redis，否则可能报错,并且要删除验证码
            redisService.set("user:userinfo:" + userInfoVO.getId(),userInfoVO);
            redisService.del("code:"+email);
            String accessToken = getOAuthToken(userInfoVO);
            if(accessToken == null){
                return Result.failMsg("登录失败，请重试");
            }
            userInfoVO.setAccessToken(accessToken);
            userInfoVO.setEmail(email);
            userInfoVO.setPassword(password);
            log.info(String.valueOf(userInfoVO));

            return Result.OK(userInfoVO);
        }else{
            return Result.failMsg("账号不存在!");
        }
    }

    @Override
    public UserInfoVO getByEmail(String email){
        UserInfoVO userInfoVO = null;
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getEmail,email).last("LIMIT 1");
        UserInfo userInfo = getOne(queryWrapper);
        if(userInfo != null){
            userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo,userInfoVO);
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());
            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
        }
        return userInfoVO;
    }

    /**
     * 获取用户详情
     */
    @Override
    public UserInfoVO getUserInfoById(Long id) {
        UserInfoVO userInfoVO = null;
        //方式1
        UserInfo userInfo = getById(id);
        if(userInfo != null){
            userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo,userInfoVO);
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());
            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
        }
        return userInfoVO;
    }

    /**
     * 调用OAuth2的获取令牌接口
     *
     * @description 1.将用户信息存入公共map中 2.获取访问令牌 3.写入"刷新令牌"到数据库
     *
     */
    private String getOAuthToken(UserInfoVO userInfoVO) {
        //获取OAuth2框架的配置信息，用于访问刷新令牌接口
        Map<String, String> tokenMap = tokenConfig.getConfig();
        Map<String,String> mapParam = new HashMap<>();
        mapParam.put("email", userInfoVO.getEmail());
        mapParam.put("nickname", userInfoVO.getNickname());
        mapParam.put("password", userInfoVO.getOriginalPassword());
        mapParam.put("client_id", tokenMap.get("clientId"));
        mapParam.put("client_secret", tokenMap.get("secret"));
        mapParam.put("grant_type", tokenMap.get("grantTypes"));
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> mapResult = restTemplate
                    .getForObject(
                            "http://localhost:"+port+"/oauth/token?username={email}&password={password}&client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}",
                            Map.class, mapParam);
            if(mapResult != null){
                try {
                    setRefreshToken(userInfoVO.getId(), mapResult.get("refresh_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mapResult.get("access_token");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户的刷新令牌
     *
     */
    @Override
    public void setRefreshToken(Long userId, String refreshToken){
        if(refreshToken != null){
            redisService.set("refresh_token:" + userId,refreshToken);
        }
    }

    /**
     * 用户注册
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfoVO> userRegister(String email,String emailCode, String password, String confirmPassword){
        UserInfoVO userInfoVO = getByEmail(email);
        if(userInfoVO != null){
            return Result.failMsg("邮箱已注册，请修改");
        }
        if (!checkEmail(email)) {
            return Result.failMsg("请输入正确的邮箱");
        }
        if(!emailCode.isEmpty()){
            // 从 Redis 中获取验证
            String redisCode = (String) redisService.get("code:" + email);
            if (redisCode == null) {
                return Result.failMsg("验证码已过期，请重新获取");
            }
            // 比较验证码是否正确
            if(!redisCode.equals(emailCode)) {
                return Result.failMsg("验证码错误，请重新输入");
            }
        }
        if(!password.equals(confirmPassword)){
            return Result.failMsg("两次输入密码不一致，请重新输入");
        }
        LocalDateTime now = LocalDateTime.now();
        userInfoVO = new UserInfoVO();
        userInfoVO.setId(IdWorker.getId());
        userInfoVO.setEmail(email);
        userInfoVO.setNickname(fileUploadConfig.getDefaultNickname());
        userInfoVO.setOriginalPassword(password);
        userInfoVO.setPassword(new BCryptPasswordEncoder().encode(password));
        userInfoVO.setAvatar(fileUploadConfig.getDefaultAvatar());
        userInfoVO.setGender(Gender.DUNNO.getValue());
        userInfoVO.setStatus(YesOrNo.NO.getValue());
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
        if (userInfo != null) {
            if (!currentPassword.equals(userInfo.getOriginalPassword())) {
                return Result.failMsg("当前密码不正确！");
            }
            if (!newPassword.equals(confirmPassword)) {
                return Result.failMsg("新密码与确认密码不匹配！");
            }
            if (currentPassword.equals(newPassword)){
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
        return Result.failMsg("用户不存在！");
    }
    /**
     * 用户列表
     */
    @Override
    public Map<Long, UserInfoVO> listByIds(List<Long> ids){
        Map<Long, UserInfoVO> userInfoVOMap = new HashMap<>();
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserInfo::getId,ids);
        List<UserInfo> userInfos = list(queryWrapper);
        userInfos.forEach(item-> {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(item,userInfoVO);
            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
            userInfoVOMap.put(userInfoVO.getId(), userInfoVO);
        });
        return userInfoVOMap;
    }

    /**
     * 更新用户信息
     */
    @Override
    public Result<UserInfoVO> update(Long userId, String nickname, MultipartFile file, Integer  gender, String hobby, String remark){
        UserInfo userInfo = getById(userId);
        if(userInfo != null){
            if(StringUtils.isEmpty(nickname)){
                return Result.failMsg("请输入昵称");
            }
            Gender genderObj = Gender.valueOf(gender);
            if(genderObj == null){
                return Result.failMsg("请选择性别");
            }
            userInfo.setNickname(nickname);
            userInfo.setGender(genderObj.getValue());
            userInfo.setHobby(hobby);
            userInfo.setRemark(remark);
            if(file != null){
                String avatar = fileUploadService.uploadImage(file);
                userInfo.setAvatar(avatar);
            }
            saveOrUpdate(userInfo);

            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo,userInfoVO);
            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
            userInfoVO.setGenderText(genderObj.getText());
            return Result.OK("保存成功",userInfoVO);
        }
        return Result.failMsg("保存失败，请重试");
    }

    /**
     * 获取用户详情
     */
    @Override
    public UserInfoVO getUserInfoAndFansById(Long id) {
        UserInfoVO userInfoVO = null;
        UserInfo userInfo = getById(id);
        if(userInfo != null){
            userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo,userInfoVO);
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());
            userInfoVO.setAvatar(fileUploadService.getImgPath(userInfoVO.getAvatar()));
            int followNum = followService.getFollowNumByUserId(userInfoVO.getId());
            userInfoVO.setFollowNum(followNum);
            int fansNum = followService.getFansNumByUserId(userInfoVO.getId());
            userInfoVO.setFansNum(fansNum);
        }
        return userInfoVO;
    }
}
