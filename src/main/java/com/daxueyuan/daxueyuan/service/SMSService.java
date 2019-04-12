package com.daxueyuan.daxueyuan.service;

/**
 * @Author: Sean
 * @Date: 2019/4/11 13:11
 */
public interface SMSService {
    void sendMessage(String phone);
    boolean validate(String phone,String code);
}
