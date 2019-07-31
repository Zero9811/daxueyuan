package com.daxueyuan.controller;

import com.daxueyuan.VO.ResultVO;
import com.daxueyuan.entity.UserRegister;
import com.daxueyuan.service.SMSService;
import com.daxueyuan.service.UserRegisterService;
import com.daxueyuan.util.ResultVOUtil;
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
        return ResultVOUtil.returnResult(12
                , "操作成功", "操作成功");
    }

    @GetMapping("/userSMS")
    public ResultVO userSendMessage(String phone){
        UserRegister userRegister = userRegisterService.findByAccount(phone);
        if (userRegister == null){
            return ResultVOUtil.returnResult(11
                    , "用户尚未注册", "用户尚未注册");
        }

        smsService.sendMessage(phone);

        return ResultVOUtil.returnResult(12
                , "操作成功", "操作成功");
    }

    @GetMapping("/registerSMS")
    public ResultVO registerSendMessage(String phone){
        UserRegister userRegister = userRegisterService.findByAccount(phone);
        if (userRegister != null){
            return ResultVOUtil.returnResult(11
                    , "用户已注册", "用户已注册");
        }

        smsService.sendMessage(phone);

        return ResultVOUtil.returnResult(12
                , "操作成功", "操作成功");
    }

    @GetMapping("/validate")
    public ResultVO validate(String phone,String code){
        if (smsService.validate(phone,code)){
            return ResultVOUtil.returnResult(12
                    , "操作成功", "验证通过");
        }
        else {
            return ResultVOUtil.returnResult(11
                    , "验证不通过", "验证不通过");
        }
    }
}
