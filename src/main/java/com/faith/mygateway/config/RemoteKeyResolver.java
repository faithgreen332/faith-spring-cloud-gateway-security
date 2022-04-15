package com.faith.mygateway.config;

import com.faith.mygateway.config.resolvers.CarRemoteKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Leeko
 * @date 2022/4/13
 **/
@Configuration
public class RemoteKeyResolver {

    @Bean("carKeyResolver")
    public CarRemoteKeyResolver carKeyResolver() {
        return new CarRemoteKeyResolver();
    }

}
