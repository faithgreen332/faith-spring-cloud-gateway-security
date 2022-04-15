package com.faith.mygateway.security.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * t_user
 *
 * @author
 */
@Data
public class TUser implements Serializable {

    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 加密后的用户密码
     */
    private String password;

    /**
     * 用户的简单描述
     */
    private String description;

    /**
     * 用户的权限，user 对象里没有，通过关联查询获取
     */
    private List<TRole> roles;

    /**
     * 用户权限，user 对象里没有，通过关联查询获取
     */
    private List<TEnum> authorities;

    /**
     * 账户是否过期，默认不过期
     */
    private Boolean accountExpiredFlag;

    /**
     * 账户是否被锁住，默认不锁
     */
    private Boolean accountLockFlag;

    /**
     * 密码是否过期，默认不过期
     */
    private Boolean credentialExpiredFlag;

    /**
     * 是否有效，默认是
     */
    private Boolean enabledFlag;

    private static final long serialVersionUID = 1L;
}