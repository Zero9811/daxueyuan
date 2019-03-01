package com.daxueyuan.daxueyuan.service.impl;

import com.daxueyuan.daxueyuan.entity.UserInfo;
import com.daxueyuan.daxueyuan.repository.UserInfoRepository;
import com.daxueyuan.daxueyuan.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Sean
 * @Date: 2019/3/1 0:58
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByAccount(String account) {
        return userInfoRepository.findByAccount(account);
    }

    @Override
    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}
