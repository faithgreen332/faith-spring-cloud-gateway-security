package com.faith.mygateway.security.service;


import com.faith.mygateway.security.dto.TUser;

/**
 * @author Leeko
 * @date 2022/3/29
 **/
public interface TokenService {

    /**
     * token 前缀
     */
    String TOKEN_PREFIX = "token_";

    /**
     * payload 的 merchantId key
     */
    String PAYLOAD_KEY_ID = "merchantId";

    /**
     * payload 的过期时间 key
     */
    String PAYLOAD_KEY_EXPAT = "expAt";

    default String getKey(String token) {
        return TOKEN_PREFIX + token;
    }

    /**
     * 新建 token
     *
     * @param token token
     * @param user  用来构建 payload 的数据源
     */
    void cacheToken(String token, TUser user);

    /**
     * 带过期时间的缓存，存到数据库的格式是：
     * key：token_{token字符串}
     * value：user 对象
     *
     * @param token token
     * @param user  user
     */
    void cacheTokenWithExpire(String token, TUser user);

    /**
     * 根据 token 取出来 TUser 对象
     *
     * @param token token
     * @return TUser
     */
    TUser parseToken2User(String token);

    /**
     * 延长 token 有效期
     *
     * @param token token
     */
    void expandTokenExp(String token);

    /**
     * 是否需要延长 token 有效期
     *
     * @param token
     * @return
     */
    boolean needExpandExp(String token);

    /**
     * 生成 token
     *
     * @param user 登录用户
     * @return token
     */
    String createToken(TUser user);

    /**
     * 判断缓存是否存在
     *
     * @param key key
     * @return 存在 true，不存在 false
     */
    boolean exists(String key);

    /**
     * 校验 token
     *
     * @param token token
     * @return true、false
     */
    boolean verify(String token);
}
