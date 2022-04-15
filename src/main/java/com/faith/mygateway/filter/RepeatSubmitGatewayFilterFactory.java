package com.faith.mygateway.filter;

import com.faith.mygateway.security.config.FunRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 相同的用户，get 请求在 15 秒内只能提交一次，防止人家狂点
 *
 * @author Leeko
 * @date 2022/4/15
 **/
@Component
public class RepeatSubmitGatewayFilterFactory implements GlobalFilter, Ordered {

    public static final String REPEAT_GET_SUBMIT_PREFIX = "repeat_get_submit_";

    @Value("${same.get-request.submit.window-sec}")
    String windowSecs;
    @Value("${token.header-name}")
    String tokenHeader;

    @Autowired
    FunRedisClient redisClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String methodValue = exchange.getRequest().getMethodValue();
        if (!HttpMethod.GET.matches(methodValue)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(tokenHeader);
        String path = exchange.getRequest().getURI().getPath();
        String queryParams = exchange.getRequest().getQueryParams().toSingleValueMap().toString();
        String key = REPEAT_GET_SUBMIT_PREFIX + token + "_" + path + "_" + queryParams;
        // 如果已经存在了，就限制不让捣乱
        if (redisClient.exists(key)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            return response.writeWith(Mono.fromSupplier(() -> {
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                // TODO 当然不能这么生硬的给字符串，这么搞的话前端的小伙伴要杀人了，需要包装成对象的
                return dataBufferFactory.wrap("您操作太频繁了，请 15 秒后再试".getBytes(StandardCharsets.UTF_8));
            }));
        } else {
            // 没有的话就记录一个
            redisClient.setWithExpire(key, String.valueOf(System.currentTimeMillis()), Long.parseLong(windowSecs), TimeUnit.SECONDS);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
