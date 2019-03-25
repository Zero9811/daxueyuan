package com.daxueyuan.daxueyuan.converter;

import com.daxueyuan.daxueyuan.entity.OrderRecord;
import com.daxueyuan.daxueyuan.form.OrderForm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/3/12 12:03
 */
@Component
public class OrderForm2OrderRecord {
    public OrderRecord convert(OrderForm orderForm) {
        OrderRecord orderRecord = new OrderRecord();
        BeanUtils.copyProperties(orderForm,orderRecord);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = simpleDateFormat.parse(orderForm.getReceiveTime());
//        orderRecord.setReceiveTime(date);
        return orderRecord;
    }
}
