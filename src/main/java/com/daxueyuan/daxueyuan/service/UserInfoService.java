package com.daxueyuan.daxueyuan.service;

import com.daxueyuan.daxueyuan.entity.UserInfo;

/**
 * @Author: Sean
 * @Date: 2019/3/1 0:57
 */
public interface UserInfoService {
    UserInfo findByAccount(String account);
    void save(UserInfo userInfo);
    void deleteByAccount(String account);
}
