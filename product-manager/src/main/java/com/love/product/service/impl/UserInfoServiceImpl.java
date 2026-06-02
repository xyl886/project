package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.Posts;
import com.love.product.entity.UserAuth;
import com.love.product.entity.UserInfo;
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
import cn.dev33.satoken.stp.StpUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        try {
            Assert.isTrue(matches, EMAIL_ERROR.getMsg());
        } catch (IllegalArgumentException e) {
            throw new BizException(EMAIL_ERROR);
        }
    }

    private void checkCode(String key, String emailCode) {
        Object code = redisService.get(key);
        try {
            Assert.isTrue(code != null && code.equals(emailCode), ERROR_EXCEPTION_MOBILE_CODE.getMsg());
        } catch (IllegalArgumentException e) {
            throw new BizException(ERROR_EXCEPTION_MOBILE_CODE);
        }
    }

    private void isValidType(String type) {
        try {
            CodeType emailCodeType = CodeType.valueOf(type.toUpperCase());
            log.info("邮箱验证码类型为：" + emailCodeType);
        } catch (IllegalArgumentException e) {
            throw new BizException(PARAMS_ILLEGAL);
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param type
     * @return
     */
    @Override
    public void sendCode(String email, String type) {
        isValidType(type);
        checkEmail(email);
        String code = getRandomCode();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "尊敬的用户" + email + "，您好!<br><br>" +
                "您正在校园墙进行<span style='font-weight:bold;'>" + CodeType.getType(type) +
                "</span>操作，本次请求的邮件验证码是：<br><br><span style='font-weight:bold; font-size:25px;'>" + code +
                "</span><br><br>本验证码5分钟内有效，为了保证您账号的安全性，请及时输入。请不要告诉他人哦！");
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("校园墙" + CodeType.getType(type) + "验证码")
                .template("common.html")
                .commentMap(map)
                .build();
        rabbitTemplate.convertAndSend(
                EMAIL_EXCHANGE,
                "*",
                new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        redisService.set(EMAIL_CODE + email, code, 5 * 60);
    }

    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    @Override
    public UserVO login(LoginVO loginVO) {
        UserInfoVO userInfoVO = getByEmail(loginVO.email);
        if (userInfoVO == null) {
            throw new BizException("账号不存在!");
        }
        if (userInfoVO.getDeleted().equals(YesOrNo.YES.getValue())) {
            throw new BizException("登录失败，账号已注销");
        }
        if (userInfoVO.getStatus().equals(YesOrNo.YES.getValue())) {
            throw new BizException("登录失败，账号已禁用，请联系客服人员");
        }
        if (loginVO.emailCode != null && !loginVO.emailCode.isEmpty() && loginVO.password.isEmpty()) {
            checkCode(EMAIL_CODE + loginVO.email, loginVO.emailCode);
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(loginVO.password, userInfoVO.password);
            if (!matches) {
                throw new BizException("账号或密码错误");
            }
        }
        // Sa-Token 登录 + 缓存用户信息到Redis
        StpUtil.login(userInfoVO.getId());
        redisService.set(USER_INFO + userInfoVO.getId(), userInfoVO, 7 * 24 * 3600L);
        redisService.del(EMAIL_CODE + loginVO.email);

        userInfoVO.setAccessToken(StpUtil.getTokenValue());
        userInfoVO.setEmail(loginVO.email);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userInfoVO, userVO);
        return userVO;
    }

    /**
     * 用户注册
     *
     * @return Result<UserInfo>
     */
    @Override
    public UserInfoVO userRegister(RegisterVO registerVO) {
        UserInfoVO userInfoVO = getByEmail(registerVO.email);
        if (userInfoVO != null) {
            throw new BizException("邮箱已注册，请修改！");
        }
        checkEmail(registerVO.email);
        if (registerVO.emailCode.isEmpty()) {
            throw new BizException("请输入验证码！");
        }
        if (registerVO.password.isEmpty()) {
            throw new BizException("请设置密码！");
        }
        checkCode(EMAIL_CODE + registerVO.email, registerVO.emailCode);
        if (!registerVO.password.equals(registerVO.confirmPassword)) {
            throw new BizException("两次输入密码不一致，请重新输入！");
        }
        LocalDateTime now = LocalDateTime.now();
        userInfoVO = new UserInfoVO();
        userInfoVO.setId(IdWorker.getId());
        userInfoVO.setEmail(registerVO.email);
        userInfoVO.setNickname("用户");
        userInfoVO.setPassword(new BCryptPasswordEncoder().encode(registerVO.password));
        userInfoVO.setAvatar("");
        userInfoVO.setGender(Gender.DUNNO.getValue());
        userInfoVO.setStatus(YesOrNo.NO.getValue());
        userInfoVO.setRole(Role.VISITOR.getValue());
        userInfoVO.setDeleted(YesOrNo.NO.getValue());
        userInfoVO.setCreateTime(now);
        userInfoVO.setUpdateTime(now);
        save(userInfoVO);
        return userInfoVO;
    }

    /**
     * 修改密码
     */
    @Override
    public void updatePwd(Long id, String currentPassword, String newPassword, String confirmPassword) {
        UserInfo userInfo = getById(id);
        if (userInfo == null) {
            throw new BizException("用户不存在！");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(currentPassword, userInfo.password);
        if (!matches) {
            throw new BizException("当前密码不正确！");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BizException("新密码与确认密码不匹配！");
        }
        if (currentPassword.equals(newPassword)) {
            throw new BizException("您的密码并未改动！");
        }
        if (!CommonUtil.isValidPassword(newPassword)) {
            throw new BizException("新密码不符合要求！");
        }
        userInfo.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        saveOrUpdate(userInfo);
    }

    /**
     * 重置密码
     *
     * @param userId
     * @param resetVO
     * @return
     */

    @Override
    public void reset(Long userId, RegisterVO resetVO) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            throw new BizException("用户异常，请联系管理员！");
        }
        if (!resetVO.password.equals(resetVO.confirmPassword)) {
            throw new BizException("新密码与确认密码不匹配！");
        }
        if (!CommonUtil.isValidPassword(resetVO.password)) {
            throw new BizException("新密码不符合要求！");
        }
        userInfo.setPassword(new BCryptPasswordEncoder().encode(resetVO.password));
        saveOrUpdate(userInfo);
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
            userInfoVO.setGenderText(Gender.valueOf(userInfoVO.gender).getText());
            userInfoVO.setFansNum(followService.getFansNumByUserId(userInfo.id));
            userInfoVO.setFollowNum(followService.getFollowNumByUserId(userInfo.id));
//            userInfoVO.setRoleName(Role.valueOf(userInfo.role).getText());
        }
        return userInfoVO;
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
    public UserInfoVO update(Long userId, String nickname, MultipartFile file, Integer gender, String hobby, String remark) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            throw new BizException("用户不存在，请重试");
        }
        if (StringUtils.isEmpty(nickname)) {
            throw new BizException("请输入昵称");
        }
        Gender genderObj = Gender.valueOf(gender);
        if (genderObj == null) {
            throw new BizException("请选择性别");
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
        userInfoVO.setGenderText(genderObj.getText());
        return userInfoVO;
    }

    /**
     * 获取单个用户详情
     */
    @Override
    public UserInfoVO getUserInfoAndFansById(Long id) {
        // 先查 Redis 缓存
        String cacheKey = USER_INFO + id;
        Object cached = redisService.get(cacheKey);
        if (cached instanceof UserInfoVO) {
            return (UserInfoVO) cached;
        }
        // 缓存未命中，查 DB
        UserInfo userInfo = getById(id);
        if (userInfo == null) {
            throw new BizException("用户不存在!");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO);
        Gender gender = Gender.valueOf(userInfoVO.getGender());
        userInfoVO.setGenderText(gender.getText());
        int followNum = followService.getFollowNumByUserId(userInfoVO.getId());
        userInfoVO.setFollowNum(followNum);
        int fansNum = followService.getFansNumByUserId(userInfoVO.getId());
        userInfoVO.setFansNum(fansNum);
        // 写入缓存，1小时过期
        redisService.set(cacheKey, userInfoVO, 3600L);
        return userInfoVO;
    }

    @Override
    public UserInfoVO getUserProfile(Long targetId, Long currentUserId) {
        UserInfoVO vo = getUserInfoAndFansById(targetId);
        vo.setFollowed(currentUserId != null && !currentUserId.equals(targetId)
                && followService.getDetail(currentUserId, targetId) != null);
        return vo;
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
    public UserInfoVO getUserById(Integer id) {
        UserInfo userInfo = getById(id);
        if (userInfo == null) {
            throw new BizException("用户不存在");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }

    @Override
    public void updateUser(UserInfo user) {
        Assert.isTrue(!Objects.equals(JwtUtil.getUserId(), user.id), "无权限！");
        baseMapper.updateById(user);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BizException("请选择要删除的用户");
        }
        removeByIds(ids);
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        Long userId = JwtUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            throw new BizException("用户不存在");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }

    @Override
    public void updatePassword(Map<String, String> map) {
        Long userId = JwtUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }
        String oldPwd = map.get("oldPassword");
        String newPwd = map.get("newPassword");
        if (oldPwd == null || newPwd == null) {
            throw new BizException("参数不完整");
        }
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            throw new BizException("用户不存在");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPwd, userInfo.password)) {
            throw new BizException("原密码不正确");
        }
        userInfo.setPassword(encoder.encode(newPwd));
        saveOrUpdate(userInfo);
    }

    @Override
    public List<Map<String, Object>> searchUser(String keyword) {
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<UserInfo>()
                .like(UserInfo::getNickname, keyword).or().like(UserInfo::getEmail, keyword)
                .last("LIMIT 20");
        List<UserInfo> list = list(qw);
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserInfo u : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId()); m.put("nickname", u.getNickname()); m.put("avatar", u.getAvatar());
            result.add(m);
        }
        return result;
    }
}
