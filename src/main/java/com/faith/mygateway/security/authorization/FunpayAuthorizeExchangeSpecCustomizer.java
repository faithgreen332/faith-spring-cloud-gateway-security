package com.faith.mygateway.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * 握翘啊，这里是启动的时候就配置好了，不能动态配啊，想先登录，拿到用户的权限，然后动态配置规则，妈的这里做不到啊，要先搞这里，才到登录
 *
 * @author Leeko
 * @date 2022/4/12
 **/
@Component
@Slf4j
public class FunpayAuthorizeExchangeSpecCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {

    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
//        ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {
//            Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
//            Iterator<? extends GrantedAuthority> iterator = authorities.stream().iterator();
//            GrantedAuthority next;
//            while (iterator.hasNext()) {
//                next = iterator.next();
//                System.out.println("aaaaa " + next.getAuthority());
//                String[] authorityPare = next.getAuthority().split("_");
//                // authority 是 url_请求方法 的形式给的，所以，照模样取出来
//                authorizeExchangeSpec.pathMatchers(authorityPare[1], authorityPare[0]).permitAll();
//            }
//            return null;
//        });
        authorizeExchangeSpec.pathMatchers("/login", "/logout").permitAll();
//        authorizeExchangeSpec.pathMatchers("/demo").hasAnyRole("aadmin");
        authorizeExchangeSpec.pathMatchers("GET", "/gateway/demo").permitAll();
//        authorizeExchangeSpec.anyExchange().hasAnyRole("developer");
    }
}
