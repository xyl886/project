package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.dto.EmailLoginDTO;
import com.shiyi.dto.EmailRegisterDTO;
import com.shiyi.dto.QQLoginDTO;
import com.shiyi.dto.UserAuthDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
public interface UserAuthService extends IService<UserAuth> {

    Result emailRegister(EmailRegisterDTO emailRegisterDTO);

    Result updatePassword(EmailRegisterDTO emailRegisterDTO);

    Result emailLogin(EmailLoginDTO emailLoginDTO);

    Result qqLogin(QQLoginDTO qqLoginDTO);

    Result weiboLogin(String code);

    Result giteeLogin(String code);

    Result sendEmailCode(String email);

    Result bindEmail(UserAuthDTO vo);

    Result updateUser(UserAuthDTO vo);

}
