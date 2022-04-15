package com.faith.mygateway.security;

import com.faith.mygateway.security.authorization.FunpayAuthorizeExchangeSpecCustomizer;
import com.faith.mygateway.security.contextrepository.FunpaySecurityContextRepository;
import com.faith.mygateway.security.handler.FunpayAccessDeniedHandler;
import com.faith.mygateway.security.handler.FunpayAuthenticationEntryPointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Leeko
 * @date 2022/3/30
 **/
@Configuration
@EnableWebFluxSecurity
public class FluxSecurityConfig {

    @Autowired
    FunpaySecurityContextRepository funpaySecurityContextRepository;

    @Autowired
    FunpayAccessDeniedHandler funpayAccessDeniedHandler;

    @Autowired
    FunpayAuthenticationEntryPointHandler funpayAuthenticationEntryPointHandler;

    @Autowired
    FunpayAuthorizeExchangeSpecCustomizer funpayAuthorizeExchangeSpecCustomizer;


    public static final String[] AUTH_WHITELIST = new String[]{"/login", "/logout"};

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf().disable();
        http.httpBasic().disable();
        http.securityContextRepository(funpaySecurityContextRepository);
        http.authorizeExchange(funpayAuthorizeExchangeSpecCustomizer)
                .exceptionHandling().authenticationEntryPoint(funpayAuthenticationEntryPointHandler)
                .and()
                .exceptionHandling().accessDeniedHandler(funpayAccessDeniedHandler);
        return http.build();
    }

    /**
     * BCrypt密码编码
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
