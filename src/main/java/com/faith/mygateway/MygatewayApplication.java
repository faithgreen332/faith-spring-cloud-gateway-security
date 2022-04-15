package com.faith.mygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class MygatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MygatewayApplication.class, args);
    }

}
