package com.love.product.config.security;

import com.love.product.entity.UserInfo;
import com.love.product.enumerate.YesOrNo;
import com.love.product.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息获取（用户名称，密码，权限）
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        //这个地方可以通过username从数据库获取正确的用户信息，包括密码和权限等。
        // 从user获取正确的用户信息，包括密码和权限等。

        // 查询用户
        UserInfo userInfo = userInfoService.getByEmail(username);
        if (userInfo != null) {
            if(userInfo.getDeleted().equals(YesOrNo.YES.getValue())){
                throw new UsernameNotFoundException("登录失败，账号已注销");
            }
            if(userInfo.getStatus().equals(YesOrNo.YES.getValue())){
                throw new UsernameNotFoundException("登录失败，账号已禁用，请联系客服人员");
            }
            String PASSWORD = "{noop}" + userInfo.getOriginalPassword();
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            return new User(username, PASSWORD, grantedAuthorityList);

        }else{
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }

    }


}
