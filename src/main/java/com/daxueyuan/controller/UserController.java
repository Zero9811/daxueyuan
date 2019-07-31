package com.daxueyuan.controller;

import com.daxueyuan.VO.ResultVO;
import com.daxueyuan.constant.RedisConstant;
import com.daxueyuan.entity.OrderRecord;
import com.daxueyuan.entity.UserInfo;
import com.daxueyuan.entity.UserRegister;
import com.daxueyuan.service.OrderService;
import com.daxueyuan.service.SMSService;
import com.daxueyuan.service.UserInfoService;
import com.daxueyuan.service.UserRegisterService;
import com.daxueyuan.util.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private OrderService orderService;

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
        ResultVO resultVO = new ResultVO();
        //已存在
        if (userRegister != null){
            resultVO.setCode(10);
            resultVO.setData("该账号已被注册");
            return resultVO;
        }
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
            return ResultVOUtil.returnResult(12, "注册成功", userInfo);
        }
        else {
            return ResultVOUtil.returnResult(11
                    , "短信验证输入错误", "短信验证输入错误");
        }
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
    public ResultVO loginByPhone(String account, String verificationCode, HttpServletResponse response){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        if (userRegister == null){
            return ResultVOUtil.returnResult(10, "该用户不存在", "该用户不存在");
        }
        //短信验证
        if(verificationCode.equals(stringRedisTemplate.opsForValue().
                get(String.format(RedisConstant.SMS_TEMPLATE,account)))){
            //Redis操作
            String token = UUID.randomUUID().toString();
            setRedis(token,account);

//            //设置cookie
//            response.addCookie(setCookie("token",token));
            return ResultVOUtil.returnResult(12
                    , "操作成功", userInfoService.findByAccount(account));
        }
        return ResultVOUtil.returnResult(11, "验证失败", "验证失败");
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
    public ResultVO loginByPassword(String account,String password,HttpServletResponse response){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        if(userRegister == null){
            return ResultVOUtil.returnResult(10, "该用户不存在", "该用户不存在");
        }
        if (!password.equals(userRegister.getPassword())){
            return ResultVOUtil.returnResult(11, "用户名或密码不正确", "用户名或密码不正确");
        }
        //Redis操作
        String token = UUID.randomUUID().toString();

        setRedis(token,account);

//        //设置cookie
//        response.addCookie(setCookie("token",token));
        return ResultVOUtil.returnResult(12
                , "操作成功", userInfoService.findByAccount(account));
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
        boolean flag = userRegisterService.isExist(account);
        if (flag){
            return ResultVOUtil.returnResult(11
                    , "该用户已注册", "该用户已注册");
        }
        return ResultVOUtil.returnResult(12
                , "该用户未注册", "该用户未注册");
    }

    /**
     * 登出
     * 1.清除Redis
     * 2.清除Cookie
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public ResultVO logout(HttpServletRequest request, HttpServletResponse response){


//        //获取cookie
//        Cookie cookie = CookieUtil.get(request,"token");
//
//        //清除Redis
//        stringRedisTemplate.delete(String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue()));
//        //清除cookie
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        cookie.setValue(null);
//        response.addCookie(cookie);
        return ResultVOUtil.returnResult(12
                , "退出登录成功", "退出登录成功");
    }

    @PutMapping("/modifyPassword")
    public ResultVO modifyPassword(String account,String oldPassword,String newPassword){
        UserRegister userRegister = userRegisterService.findByAccount(account);
        if (userRegister != null){
            if (userRegister.getPassword().equals(oldPassword)){
                userRegister.setPassword(newPassword);
                userRegisterService.save(userRegister);
                return ResultVOUtil.returnResult(12
                        , "操作成功", "操作成功");
            }
            else {
                return ResultVOUtil.returnResult(11
                        , "操作失败", "操作失败");
            }
        }
        else {
            return ResultVOUtil.returnResult(10
                    , "用户不存在", "用户不存在");
        }
    }

    @PutMapping("/modifyUserInfo")
    public ResultVO modifyUserInfo(UserInfo userInfo){
        userInfoService.save(userInfo);
        return ResultVOUtil.returnResult(12
                , "操作成功", userInfo);
    }

    @PutMapping("/phonePasswordReset")
    public ResultVO phonePasswordReset(String account,String verificationCode,String newPassword){
        if(!verificationCode.equals(stringRedisTemplate.opsForValue().
                get(String.format(RedisConstant.SMS_TEMPLATE,account)))){
            return ResultVOUtil.returnResult(11
                    , "验证码错误", "验证码错误");
        }

        UserRegister userRegister = userRegisterService.findByAccount(account);
        userRegister.setPassword(newPassword);
        userRegisterService.save(userRegister);
        return ResultVOUtil.returnResult(12
                , "操作成功", "操作成功");
    }

    //旧手机和新手机都要验证

    /**
     * 查看新的手机号是否已注册
     * 验证验证码是否通过
     * 添加新账号
     * 将所有相关信息改为新账号
     * 删除旧账号
     * @param oldAccount
     * @param newAccount
     * @param verificationCode
     * @return
     */
    @PutMapping("/account")
    public ResultVO changeAccount(String oldAccount,String newAccount,String verificationCode){
        //验证
        if (userRegisterService.isExist(newAccount)){
            return ResultVOUtil.returnResult(10
                    , "账号已存在", "账号已存在");
        }
        if (!smsService.validate(newAccount,verificationCode)){
            return ResultVOUtil.returnResult(11
                    , "验证不通过", "验证码错误");
        }
        //保存
        UserRegister userRegister = userRegisterService.findByAccount(oldAccount);
        UserRegister userRegister1 = new UserRegister();
        BeanUtils.copyProperties(userRegister,userRegister1);
        userRegister1.setAccount(newAccount);
        userRegisterService.save(userRegister1);
        UserInfo userInfo = userInfoService.findByAccount(oldAccount);
        UserInfo userInfo1 = new UserInfo();
        BeanUtils.copyProperties(userInfo,userInfo1);
        userInfo1.setAccount(newAccount);
        userInfoService.save(userInfo1);

        List<OrderRecord> orderList1 = orderService.findByCreatorAccount(oldAccount);
        if (orderList1.size()>0) {
            for (OrderRecord orderRecord :
                    orderList1) {
                orderRecord.setCreatorAccount(newAccount);
            }
            orderService.saveAll(orderList1);
        }
        List<OrderRecord> orderList2 = orderService.findByReceiverAccount(oldAccount);
        if (orderList2.size()>0) {
            for (OrderRecord orderRecord :
                    orderList2) {
                orderRecord.setReceiverAccount(newAccount);
            }
            orderService.saveAll(orderList2);
        }

        //删除旧信息
        userRegisterService.deleteByAccount(oldAccount);
        userInfoService.deleteByAccount(oldAccount);

        return ResultVOUtil.returnResult(12
                , "操作成功", userInfo1);
    }

    private void setRedis(String key,String value){
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE,
                key),value,RedisConstant.TOKEN_EXPIRE,TimeUnit.SECONDS);
    }

    private Cookie setCookie(String key,String value){
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setMaxAge(RedisConstant.TOKEN_EXPIRE);
        return cookie;
    }
}
