package com.daxueyuan.daxueyuan.repository;

import com.daxueyuan.daxueyuan.DaxueyuanApplicationTests;
import com.daxueyuan.daxueyuan.entity.OrderRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:58
 */
@Component
public class OrderTest extends DaxueyuanApplicationTests {

    @Autowired
    private OrderRecordRepository orderRecordRepository;

    @Test
    public void saveTest(){
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setCreatorAccount("555");
        orderRecord.setCreateTime(new Date());
        orderRecord.setFromAddress("这里");
        orderRecord.setToAddress("那里");
        orderRecord.setMessage("hahah");
        orderRecord.setPackageName("不知道");
        orderRecord.setPackageCode("333");
        orderRecord.setPackageType("小");
        orderRecord.setPackagePrice(4.6);
        orderRecord.setReceiveTime(new Date());
        orderRecord.setRemark("不用");
        orderRecordRepository.save(orderRecord);
    }

    @Test
    public void query(){
        List list = orderRecordRepository.findAccessableOrder();
        System.out.println(list.size());
    }
}
