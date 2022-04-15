package com.faith.mygateway.security.service;

import com.faith.mygateway.security.dao.TUserDao;
import com.faith.mygateway.security.dto.TUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Leeko
 * @date 2022/3/29
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    final TUserDao userDao;

    public TUser selectByName(String userName) {
        return userDao.selectByUserName(userName);
    }
}
