package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.fileupload.FileUploadConfig;
import com.love.product.config.security.TokenConfig;
import com.love.product.entity.UserInfo;
import com.love.product.entity.base.Result;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.enumerate.Gender;
import com.love.product.enumerate.YesOrNo;
import com.love.product.mapper.UserInfoMapper;
import com.love.product.service.FileUploadService;
import com.love.product.service.FollowService;
import com.love.product.service.UserInfoService;
import com.love.product.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
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
    private RestTemplate restTemplate;
    @Resource
    private TokenConfig tokenConfig;
    @Resource
    private FileUploadConfig fileUploadConfig;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private FollowService followService;


    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfoVO> login(String phone, String password){
        UserInfoVO userInfoVo = getByPhone(phone);
        if(userInfoVo != null){
            if(userInfoVo.getDeleted().equals(YesOrNo.YES.getValue())){
                return Result.failMsg("登录失败，账号已注销");
            }
            if(userInfoVo.getStatus().equals(YesOrNo.YES.getValue())){
                return Result.failMsg("登录失败，账号已禁用，请联系客服人员");
            }
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, userInfoVo.getPassword());
            if (!matches) {
                return Result.failMsg("账号或密码错误");
            }
            //一定要在获取token前缓存redis，否则可能报错
            RedisUtil.setUser(userInfoVo);
            String accessToken = getOAuthToken(userInfoVo);
            if(accessToken == null){
                return Result.failMsg("登录失败，请重试");
            }
            userInfoVo.setAccessToken(accessToken);
            userInfoVo.setPhone(phone);
            userInfoVo.setPassword(password);
            log.info(String.valueOf(userInfoVo));

            return Result.OK(userInfoVo);
        }else{
            return Result.failMsg("账号或密码错误");
        }
    }

    @Override
    public UserInfoVO getByPhone(String phone){
        UserInfoVO userInfoVO = null;
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone,phone).last("LIMIT 1");
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
        mapParam.put("phone", userInfoVO.getPhone());
        mapParam.put("nickname", userInfoVO.getNickname());
        mapParam.put("password", userInfoVO.getOriginalPassword());
        mapParam.put("client_id", tokenMap.get("clientId"));
        mapParam.put("client_secret", tokenMap.get("secret"));
        mapParam.put("grant_type", tokenMap.get("grantTypes"));
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> mapResult = restTemplate
                    .getForObject(
                            "http://localhost:"+port+"/oauth/token?username={phone}&password={password}&client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}",
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
            RedisUtil.setRefreshToken(userId,refreshToken);
        }
    }

    /**
     * 用户注册
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfoVO> userRegister(String phone, String password, String confirmPassword){
        if(!password.equals(confirmPassword)){
            return Result.failMsg("两次输入密码不一致，请重新输入");
        }
        UserInfoVO userInfoVO = getByPhone(phone);
        if(userInfoVO != null){
            return Result.failMsg("手机号已存在，请修改");
        }
        LocalDateTime now = LocalDateTime.now();
        userInfoVO = new UserInfoVO();
        userInfoVO.setId(IdWorker.getId());
        userInfoVO.setPhone(phone);
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
