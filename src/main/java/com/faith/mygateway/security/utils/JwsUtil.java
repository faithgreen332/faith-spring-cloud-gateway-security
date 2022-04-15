package com.faith.mygateway.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 * jws 的相关操作
 *
 * @author Leeko
 * @date 2022/3/29
 **/
public class JwsUtil {

    /**
     * 生成 token
     *
     * @param claims    claims
     * @param secretKey secretKey
     * @return token
     */
    public static String jwsToken(Map<String, Object> claims, String secretKey) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    /**
     * 校验 token
     *
     * @param token     token
     * @param secretKey secretKey
     * @return true、false
     */
    public static boolean verifyToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取 payload 的值
     *
     * @param token      token
     * @param payLoadKey payload 的 key
     * @param secretKey  secretKey
     * @return payload 的值
     */
    public static <T> T getPayload(String token, String payLoadKey, String secretKey, Class<T> type) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(payLoadKey, type);
    }
}
