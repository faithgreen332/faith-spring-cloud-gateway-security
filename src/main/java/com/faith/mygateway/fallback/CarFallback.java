package com.faith.mygateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leeko
 * @date 2022/4/13
 **/
@RestController
public class CarFallback {

    @PostMapping("/car/fallback")
    public String fallback() {
        System.out.println("------------- 降级了");
        // TODO 这里可以加入一些人工补救的措施，比如异步发邮件通知运维去看下后台服务
        return "劳资受不鸟了，等会再来";
    }

    @GetMapping("/car/breaker/fallback")
    public String breakerFallback() {
        System.out.println("------------- 熔断了");
        // TODO 这里可以加入一些人工补救的措施，比如异步发邮件通知运维去看下后台服务
        return "熔断了，等服务恢复吧";
    }
}
