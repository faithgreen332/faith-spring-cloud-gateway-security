package com.faith.mygateway.security.handler;

import com.alibaba.fastjson.JSON;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 认证失败的 handler
 *
 * @author Leeko
 * @date 2022/3/30
 **/
@Component
@Slf4j
@Setter
public class FunpayAuthenticationEntryPointHandler implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.error("un-authentication,path = {}", exchange.getRequest().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(JSON.toJSONBytes("未通过认证,原因是：" + e.getMessage()));
        return exchange.getResponse().writeWith(Flux.just(wrap));
    }
}
