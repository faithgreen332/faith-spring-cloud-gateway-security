package com.faith.mygateway.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * @author Leeko
 * @date 2022/4/12
 **/
@Component
@Slf4j
public class FunpayAuthorizeExchangeSpecCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {
    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        authorizeExchangeSpec.pathMatchers("/login", "/logout").permitAll();
        authorizeExchangeSpec.pathMatchers("/demo").hasAnyRole("aadmin");
        authorizeExchangeSpec.anyExchange().hasAnyRole("developerr");
    }
}
