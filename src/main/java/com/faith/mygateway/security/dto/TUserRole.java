package com.faith.mygateway.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * t_user_role
 * @author 
 */
@Data
public class TUserRole implements Serializable {
    private Long id;

    /**
     * 对应 t_user_info 的 id
     */
    private Long uid;

    /**
     * 对应 t_role 里的 id
     */
    private Long rid;

    /**
     * 是否有效，默认是
     */
    private Byte enabledFlag;

    private static final long serialVersionUID = 1L;
}