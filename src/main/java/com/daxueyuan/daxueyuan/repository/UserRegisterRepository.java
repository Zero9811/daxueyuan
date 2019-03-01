package com.daxueyuan.daxueyuan.repository;

import com.daxueyuan.daxueyuan.entity.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:38
 */
public interface UserRegisterRepository extends JpaRepository<UserRegister, String> {
    UserRegister findByAccount(String account);
}
