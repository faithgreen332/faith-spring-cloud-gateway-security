package com.faith.mygateway.controller;

import com.faith.mygateway.security.service.LoginService;
import com.faith.mygateway.security.vo.LoginBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Leeko
 * @date 2022/4/14
 **/
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("login")
    public Mono<String> login(@RequestBody LoginBody body) {
        return loginService.login(body);
    }
}
