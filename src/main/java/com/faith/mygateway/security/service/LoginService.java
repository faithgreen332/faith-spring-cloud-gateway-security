package com.faith.mygateway.security.service;

import com.faith.mygateway.security.config.FunRedisClient;
import com.faith.mygateway.security.dao.TRoleDao;
import com.faith.mygateway.security.dao.TUserDao;
import com.faith.mygateway.security.dto.TUser;
import com.faith.mygateway.security.utils.TokenAuthenticationUtil;
import com.faith.mygateway.security.vo.LoginBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author Leeko
 * @date 2022/3/29
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    final TUserDao userDao;

    final TRoleDao roleDao;

    final FunRedisClient redisClient;

    final TokenService tokenService;

    @Transactional
    public Mono<String> login(LoginBody loginBody) {

        TUser tUser = userDao.selectByUserName(loginBody.getUserName());
        // TODO 空断言

        boolean matches = new BCryptPasswordEncoder().matches(loginBody.getPassword(), tUser.getPassword());
        // TODO 断言密码不正确
        // 生成 token 返回
        String token = tokenService.createToken(tUser);
        tokenService.cacheToken(token, tUser);

        Authentication authentication = TokenAuthenticationUtil.userNamePasswordAuthenticationToken(tUser);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        System.out.println("context:" + ReactiveSecurityContextHolder.getContext());

//        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext));
        ReactiveSecurityContextHolder.withAuthentication(authentication);


        return Mono.just(token);
    }
}
