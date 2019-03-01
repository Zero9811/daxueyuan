package com.daxueyuan.daxueyuan.repository;

import com.daxueyuan.daxueyuan.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:56
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
    UserInfo findByAccount(String account);
}
