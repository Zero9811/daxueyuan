package com.daxueyuan.daxueyuan.controller;

import com.daxueyuan.daxueyuan.VO.ResultVO;
import com.daxueyuan.daxueyuan.constant.RedisConstant;
import com.daxueyuan.daxueyuan.constant.SMSConstant;
import com.daxueyuan.daxueyuan.util.ResultVOUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sean
 * @Date: 2019/3/22 19:50
 */
@RestController
@RequestMapping("/sms")
@Slf4j
public class SMSController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取短信验证
     * 1.随机一个6位数
     * 2.将手机号作为Key，6位数作为Value存入Redis
     * 3.向手机发送短信
     * 4.返回结果
     * @param phone
     * @return
     */
    @GetMapping
    public ResultVO sendeMessage(String phone){
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
        System.out.println(result);
        return ResultVOUtil.success("发送成功");
    }
}
