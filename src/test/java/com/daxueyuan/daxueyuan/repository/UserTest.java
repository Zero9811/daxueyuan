package com.daxueyuan.daxueyuan.repository;

import com.daxueyuan.daxueyuan.DaxueyuanApplicationTests;
import com.daxueyuan.daxueyuan.entity.OrderRecord;
import com.daxueyuan.daxueyuan.entity.UserInfo;
import com.daxueyuan.daxueyuan.entity.UserRegister;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:46
 */
@Component
public class UserTest extends DaxueyuanApplicationTests {

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private OrderRecordRepository orderRecordRepository;

    @Test
    public void userRegisterTest(){
        UserRegister userRegister = new UserRegister();
        userRegister.setAccount("128");
        userRegister.setPassword("432");
        userRegisterRepository.save(userRegister);
    }

    @Test
    public void findTest(){
        System.out.println(new Date());
        UserRegister userRegister = userRegisterRepository.findByAccount("128");
        System.out.println(userRegister.toString());
    }

    @Test
    public void userInfoTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("123");
        userInfo.setEmail("4545@gg.com");
        userInfo.setHeadPortraid("3232");
        userInfo.setNickName("hello");
        userInfo.setSex("男");
        userInfo.setStudentState("已完成");
        userInfoRepository.save(userInfo);
    }

//    @Test
//    public void orderTest(){
//        OrderRecord orderRecord = new OrderRecord();
//        orderRecord.setCreatorAccount("222");
//        orderRecord.setCreateTime(new Date());
//        orderRecord.setFrom("这里");
//        orderRecord.setTo("那里");
//        orderRecord.setMessage("hahah");
//        orderRecord.setPackageName("不知道");
//        orderRecord.setPackageCode("333");
//        orderRecord.setPackageType("小");
//        orderRecord.setPackagePrice(4.6);
//        orderRecord.setReceiveTime(new Date());
//        orderRecord.setRemark("不用");
//        orderRecordRepository.save(orderRecord);
//    }
}
