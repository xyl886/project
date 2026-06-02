package com.love.product.service.impl;

import cn.hutool.core.util.IdUtil;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultCode;
import com.love.product.entity.dto.LoginDTO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.entity.vo.ValidateCodeVO;
import com.love.product.enums.Gender;
import com.love.product.mapper.SysUserMapper;
import com.love.product.service.LoginService;
import com.love.product.service.RedisService;
import com.love.product.service.UserInfoService;
import com.love.product.util.CommonUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.love.product.constant.RedisKeyConstant.USER_USERINFO;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: SysLoginServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/17 14:47
 */
@Service
@Slf4j
public class SysLoginServiceImpl implements LoginService {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedisService redisService;
    @Resource
    private SysUserMapper sysUserMapper;
    public void getValidateCode(Result result) {
        long startTime = System.currentTimeMillis();
        try {
            // 创建验证码文本
            ValidateCodeVO vo = new ValidateCodeVO();
            String key = IdUtil.simpleUUID();
            String code = CommonUtil.getRandomCode();
//            redisService.set(redisService.getCacheKey(CachePrefixContent.VALIDATE_CODE_PREFIX, key), code, commonConfig.getCodePeriod(), TimeUnit.SECONDS);
            vo.setKey(key);
            vo.setCode(code);
            result.setData(vo);
        } catch (Exception e) {
            log.error("生成验证码失败,{}", e);
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(ResultCode.FAILED.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【生成验证码接口】【{}ms】 \n入参:{}\n出参:{}", "生成验证码", endTime - startTime, null, result);
        }
    }
    @Override
    public Result login(LoginDTO loginDTO) {
//        //先校验验证码
//        String msg = captchaService.checkImageCode(dto.getNonceStr(),dto.getValue());
//        if (StringUtils.isNotBlank(msg)) {
//            return ResponseResult.error(msg);
//        }

        //校验用户名和密码
        UserInfoVO userInfoVO = sysUserMapper.selectNameAndPassword(loginDTO.getEmail());
        if (userInfoVO == null) {
            return Result.failMsg("用户不存在");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), userInfoVO.password)) {
            return Result.failMsg("密码错误");
        }
        // 仅允许管理员登录管理后台
        if (userInfoVO.getRole() == null || userInfoVO.getRole() != 3) {
            return Result.failMsg("无管理员权限");
        }

            // Sa-Token 登录
            StpUtil.login(userInfoVO.getId());
            //缓存用户信息到Redis
            redisService.set(USER_USERINFO + userInfoVO.getId(), userInfoVO);
            redisService.expire(USER_USERINFO + userInfoVO.getId(), 1, TimeUnit.DAYS);
            userInfoVO.setAccessToken(StpUtil.getTokenValue());
            userInfoVO.setEmail(loginDTO.getEmail());
            Gender gender = Gender.valueOf(userInfoVO.getGender());
            userInfoVO.setGenderText(gender.getText());

            return Result.OK(userInfoVO);
    }
}
