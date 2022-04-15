package com.faith.mygateway.security.contextrepository;

import com.faith.mygateway.security.dto.TUser;
import com.faith.mygateway.security.service.TokenService;
import com.faith.mygateway.security.utils.TokenAuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 从 header 里拿到 token 并放到 ReactiveAuthenticationManager
 *
 * @author Leeko
 * @date 2022/3/31
 **/
@Component
@Slf4j
public class FunpaySecurityContextRepository implements ServerSecurityContextRepository {

    @Value("${token.header-name}")
    private String authenticationParam;

    @Autowired
    private TokenService tokenService;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String accessToken = exchange.getRequest().getHeaders().getFirst(authenticationParam);
        if (!StringUtils.isEmpty(accessToken)) {
            if (tokenService.exists(accessToken) && tokenService.verify(accessToken)) {
                TUser tUser = tokenService.parseToken2User(accessToken);
                Authentication authentication = TokenAuthenticationUtil.userNamePasswordAuthenticationToken(tUser);
                SecurityContextImpl securityContext = new SecurityContextImpl();
                securityContext.setAuthentication(authentication);

                ReactiveSecurityContextHolder.withAuthentication(authentication);

                // 更新 token 有效期
                if (tokenService.needExpandExp(accessToken)) {
                    tokenService.expandTokenExp(accessToken);
                }

                return Mono.just(securityContext);
            }
        }
        return Mono.empty();
    }
}
