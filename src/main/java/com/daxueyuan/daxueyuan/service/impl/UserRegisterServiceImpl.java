package com.daxueyuan.daxueyuan.service.impl;

import com.daxueyuan.daxueyuan.entity.UserRegister;
import com.daxueyuan.daxueyuan.repository.UserRegisterRepository;
import com.daxueyuan.daxueyuan.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Sean
 * @Date: 2019/2/28 23:43
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Override
    public void save(UserRegister userRegister) {
        userRegisterRepository.save(userRegister);
    }

    @Override
    public boolean isExist(String account) {
        UserRegister userRegister = userRegisterRepository.findByAccount(account);

        return userRegister!=null;
    }

    @Override
    public UserRegister findByAccount(String account) {
        return userRegisterRepository.findByAccount(account);
    }
}
