package com.daxueyuan.daxueyuan.controller;

import com.daxueyuan.daxueyuan.VO.ResultVO;
import com.daxueyuan.daxueyuan.entity.UserRegister;
import com.daxueyuan.daxueyuan.service.SMSService;
import com.daxueyuan.daxueyuan.service.UserRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: Sean
 * @Date: 2019/3/22 19:50
 */
@RestController
@RequestMapping("/sms")
@Slf4j
public class SMSController {

    @Autowired
    private SMSService smsService;

    @Autowired
    private UserRegisterService userRegisterService;

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
    public ResultVO sendMessage(String phone){
        smsService.sendMessage(phone);
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(12);
        resultVO.setMsg("操作成功");
        return resultVO;
    }

    @GetMapping("/userSMS")
    public ResultVO userSendMessage(String phone){
        ResultVO resultVO = new ResultVO();
        UserRegister userRegister = userRegisterService.findByAccount(phone);
        if (userRegister == null){
            resultVO.setCode(11);
            resultVO.setMsg("用户尚未注册");
            return resultVO;
        }

        smsService.sendMessage(phone);

        resultVO.setCode(12);
        resultVO.setMsg("操作成功");
        return resultVO;
    }

    @GetMapping("/registerSMS")
    public ResultVO registerSendMessage(String phone){
        ResultVO resultVO = new ResultVO();
        UserRegister userRegister = userRegisterService.findByAccount(phone);
        if (userRegister != null){
            resultVO.setCode(11);
            resultVO.setMsg("用户已注册");
            return resultVO;
        }

        smsService.sendMessage(phone);

        resultVO.setCode(12);
        resultVO.setMsg("操作成功");
        return resultVO;
    }
}
