package com.daxueyuan.constant;

/**
 * @Author: Sean
 * @Date: 2019/3/1 0:32
 */
public interface RedisConstant {
    String SMS_TEMPLATE ="sms_temp_%s";
    int SMS_EXPIRE = 300;
    String TOKEN_TEMPLATE ="token_temp_%s";
    int TOKEN_EXPIRE = 360000;
}
