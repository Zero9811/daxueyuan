package com.daxueyuan.service;

import com.daxueyuan.entity.UserRegister;

/**
 * @Author: Sean
 * @Date: 2019/2/28 23:35
 */
public interface UserRegisterService {
    void save(UserRegister userRegister);
    boolean isExist(String account);
    UserRegister findByAccount(String account);
    void deleteByAccount(String account);
}
