package com.faith.mygateway.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * t_enum
 * @author 
 */
@Data
public class TEnum implements Serializable {
    private Long id;

    /**
     * 菜单名
     */
    private String enumName;

    /**
     * 菜单在应用里的地址
     */
    private String url;

    /**
     * 对菜单的描述
     */
    private String enumDescription;

    /**
     * 对此菜单可以做的操作，比如菜单可以增删改查，请遵守 restful 风格，post/del/put/get
     */
    private Object requestMethod;

    /**
     * 父菜单 id
     */
    private Long pid;

    /**
     * 是否有效标记，默认有效
     */
    private Byte enabledFlag;

    private static final long serialVersionUID = 1L;
}