package com.daxueyuan.daxueyuan.controller;

import com.daxueyuan.daxueyuan.VO.ResultVO;
import com.daxueyuan.daxueyuan.constant.RedisConstant;
import com.daxueyuan.daxueyuan.entity.UserInfo;
import com.daxueyuan.daxueyuan.entity.UserRegister;
import com.daxueyuan.daxueyuan.service.UserInfoService;
import com.daxueyuan.daxueyuan.service.UserRegisterService;
import com.daxueyuan.daxueyuan.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:27
 */
@RestController
public class UserController {

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    /**
     *注册
     * 1.查询手机号是否已被注册
     * 2.手机号和短信验证
     * 3.存入数据库
     * 4.获取UID，创建个人信息
     * @param account
     * @param verificationCode
     * @param password
     * @return
     */
    @PostMapping("/register")
    public ResultVO register(String account,String verificationCode ,String password){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        //已存在
        if (userRegister != null)
            return ResultVOUtil.fail("该账号已被注册");
        //短信验证
        if(verificationCode.equals(stringRedisTemplate.opsForValue().
                get(String.format(RedisConstant.SMS_TEMPLATE,account)))){
            //保存两个
            UserRegister u = new UserRegister();
            u.setAccount(account);
            u.setPassword(password);
            userRegisterService.save(u);
            u = userRegisterService.findByAccount(account);
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount(u.getAccount());
            userInfoService.save(userInfo);
            return ResultVOUtil.success("注册成功");
        }
        else
            return ResultVOUtil.fail("短信验证失败");
    }

    /**
     * 获取短信验证
     * 1.随机一个6位数
     * 2.将手机号作为Key，6位数作为Value存入Redis
     * 3.向手机发送短信
     * 4.返回结果
     * @param account
     * @return
     */
    @GetMapping("/message")
    public ResultVO sms(String account){

        int value = (int)((Math.random()*9+1)*100000);
        //key、value、最大过期时间、时间单位
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.
                SMS_TEMPLATE,account),String.valueOf(value),
                RedisConstant.SMS_EXPIRE, TimeUnit.SECONDS);
        //TODO 短信发送
        return ResultVOUtil.success("短信发送成功");
    }

    /**
     * 手机登录
     * 1.查询是否注册
     * 1.对比Redis
     * 2.设置token
     * 3.返回结果
     * @param account
     * @param verificationCode
     * @return
     */
    @PostMapping("/phoneLogin")
    public ResultVO loginByPhone(String account,String verificationCode){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        if (userRegister == null)
            return ResultVOUtil.fail("该用户不存在");
        //短信验证
        if(verificationCode.equals(stringRedisTemplate.opsForValue().
                get(String.format(RedisConstant.SMS_TEMPLATE,account)))){
            //TODO Redis操作
            String token = UUID.randomUUID().toString();
            HashMap hashMap = new HashMap();
            hashMap.put("token",token);
            return ResultVOUtil.success(hashMap);
        }
        return ResultVOUtil.fail("验证失败");
    }

    /**
     * 密码登录
     * 1.查询是否注册
     * 1.比较数据库
     * 2.设置token
     * 3.返回结果
     * @param account
     * @param password
     * @return
     */
    @PostMapping("/passwordLogin")
    public ResultVO loginByPassword(String account,String password){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        if(userRegister == null)
            return ResultVOUtil.fail("该用户不存在");
        if (!password.equals(userRegister.getPassword()))
            return ResultVOUtil.fail("用户名或密码不正确");
        //TODO Redis操作
        String token = UUID.randomUUID().toString();
        HashMap hashMap = new HashMap();
        hashMap.put("token",token);
        return ResultVOUtil.success(hashMap);
    }

    /**
     * 判断账号是否已被注册
     * 1.数据库对比
     * 2.都返回success，已注册会有个真布尔值，未注册会有个假布尔值
     * @param account
     * @return
     */
    @GetMapping("/isRegister")
    public ResultVO isRegister(String account){
        return userRegisterService.isExist(account)?
                ResultVOUtil.success(true):ResultVOUtil.success(false);
    }

    @GetMapping("/logout")
    public ResultVO logout(String aa){
        return null;
    }
}
