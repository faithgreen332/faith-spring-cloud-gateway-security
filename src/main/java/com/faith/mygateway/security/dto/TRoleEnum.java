package com.faith.mygateway.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * t_role_enum
 * @author 
 */
@Data
public class TRoleEnum implements Serializable {
    private Long id;

    /**
     * 对应 t_role 表的 id
     */
    private Long rid;

    /**
     * 对应 t_enum 表的 id
     */
    private Long eid;

    /**
     * 是否有效标记，默认有效
     */
    private Byte enabledFlag;

    private static final long serialVersionUID = 1L;
}