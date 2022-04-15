package com.faith.mygateway.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * t_role
 * @author 
 */
@Data
public class TRole implements Serializable {
    private Long id;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 对角色的描述
     */
    private String description;

    /**
     * 是否有效，默认是
     */
    private Boolean enabledFlag;

    private static final long serialVersionUID = 1L;
}