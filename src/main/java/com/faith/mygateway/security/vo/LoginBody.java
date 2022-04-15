package com.faith.mygateway.security.vo;

import lombok.Data;

/**
 * @author Leeko
 * @date 2022/3/31
 **/
@Data
public class LoginBody {
    private String userName;
    private String password;

    /**
     * otpCode
     */
    private String otpCode;

    /**
     * 设备唯一标识
     */
    private String fingerPrint;

    /**
     * 是否 7 天免密登录，1，是，0，否
     */
    private String isFree;

    /**
     * TODO 如果是正式环境，这里要给断言，默认是 1
     * 是否是 mfa 验证，1，是，0，否
     */
//    @MatchesPattern("1")
    private String isMfa;
}
