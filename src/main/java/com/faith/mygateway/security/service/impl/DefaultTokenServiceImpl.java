package com.faith.mygateway.security.service.impl;

import com.faith.mygateway.security.config.FunRedisClient;
import com.faith.mygateway.security.dto.TUser;
import com.faith.mygateway.security.service.TokenService;
import com.faith.mygateway.security.utils.JwsUtil;
import com.faith.mygateway.security.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Leeko
 * @date 2022/3/29
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultTokenServiceImpl implements TokenService {

    @Value("${token.secret}")
    String tokenSecret;

    @Value("${token.expire-min}")
    String expireMin;

    final FunRedisClient redisClient;

    @Override
    public void cacheToken(String token, TUser user) {
        redisClient.hmSetAll(getKey(token), user);
        log.info("cacheToken :" + token);
    }

    @Override
    public void cacheTokenWithExpire(String token, TUser user) {
        redisClient.hmSetAllExpire(getKey(token), user, Integer.parseInt(expireMin), TimeUnit.MINUTES);
        log.info("cacheTokenWithExpire :" + token);
    }

    @Override
    public String createToken(TUser user) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(PAYLOAD_KEY_ID, user.getId());
        claims.put(PAYLOAD_KEY_EXPAT, TimeUtil.futureMillByMin(Integer.parseInt(expireMin)));

        return JwsUtil.jwsToken(claims, tokenSecret);
    }

    @Override
    public boolean exists(String token) {
        return redisClient.exists(getKey(token));
    }

    @Override
    public void expandTokenExp(String token) {
        redisClient.expire(getKey(token), Long.parseLong(expireMin), TimeUnit.MINUTES);
    }

    @Override
    public boolean needExpandExp(String token) {
        return System.currentTimeMillis() >= JwsUtil.getPayload(token, PAYLOAD_KEY_EXPAT, tokenSecret, Long.class);
    }

    @Override
    public TUser parseToken2User(String token) {
        return redisClient.hmGetAll(getKey(token), TUser.class);
    }

    @Override
    public boolean verify(String token) {
        return JwsUtil.verifyToken(token, tokenSecret);
    }
}
