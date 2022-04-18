package com.faith.mygateway.security.authorization;

import com.faith.mygateway.security.dao.TEnumDao;
import com.faith.mygateway.security.dto.TEnum;
import com.faith.mygateway.security.utils.TokenAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 屌爆了，思想是这样的，预先把所有的权限配置好,等登录的用户用他自己的权限来匹配就行了
 * 权限的校验规则：方法名 + url 有 ”url_方法名“ 这样的权限，就通过，否则不通过
 *
 * @author Leeko
 * @date 2022/4/12
 **/
@Component
public class FunpayAuthorizeExchangeSpecCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {

    @Autowired
    TEnumDao enumDao;

    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        List<TEnum> tEnums = enumDao.selectAll();
        authorizeExchangeSpec.pathMatchers("/login", "/logout", "/favicon.ico", "/actuator/**").permitAll();
        tEnums.forEach(tEnum -> {
            String method, url;
            authorizeExchangeSpec.pathMatchers(method = tEnum.getRequestMethod().toString(), url = tEnum.getUrl()).hasAuthority(TokenAuthenticationUtil.constructAuthority(url, method));
        });
    }
}
