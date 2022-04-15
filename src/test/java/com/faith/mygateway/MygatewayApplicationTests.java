package com.faith.mygateway;

import com.faith.mygateway.security.config.FunRedisClient;
import com.faith.mygateway.security.vo.LoginBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class MygatewayApplicationTests {

    @Test
    void contextLoads() {
    }


    @Autowired
    FunRedisClient redisClient;

    @Test
    void redisExpire() {
        LoginBody loginBody = new LoginBody();
        loginBody.setPassword("aaa");
        redisClient.hmSetAllExpire("aaaaa", loginBody, Integer.parseInt("1"), TimeUnit.MINUTES);
    }

    @Test
    void getExpire() {
        System.out.println("是否真的失效了啊：" + redisClient.exists("aaaaa"));
    }

}
