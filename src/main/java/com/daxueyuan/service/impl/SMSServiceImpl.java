package com.daxueyuan.service.impl;

import com.daxueyuan.constant.SMSConstant;
import com.daxueyuan.constant.RedisConstant;
import com.daxueyuan.service.SMSService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sean
 * @Date: 2019/4/11 13:13
 */
@Service
@Slf4j
public class SMSServiceImpl implements SMSService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendMessage(String phone) {
        String[] phoneNumbers = {phone};
        log.info("发送的手机号为 ： {}",phoneNumbers[0]);
        //生成验证码
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<6;i++){
            int temp = (int)(Math.random()*9);
            stringBuilder.append(temp);
        }
        String key = stringBuilder.toString();

        //存储于Redis
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.SMS_TEMPLATE,
                phone),key,RedisConstant.SMS_EXPIRE, TimeUnit.SECONDS);

        String[] params = {key,"3"};

        SmsSingleSender ssender = new SmsSingleSender(SMSConstant.appid,SMSConstant.appkey);
        SmsSingleSenderResult result = null;
        try {
            result = ssender.sendWithParam("86",phoneNumbers[0],
                    SMSConstant.templateId,params,SMSConstant.smsSign,"","");
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(String phone, String code) {
        if(code.equals(stringRedisTemplate.opsForValue().
                get(String.format(RedisConstant.SMS_TEMPLATE,phone)))){
            return true;
        }
        return false;
    }
}
