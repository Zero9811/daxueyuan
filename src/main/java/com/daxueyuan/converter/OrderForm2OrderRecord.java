package com.daxueyuan.converter;

import com.daxueyuan.entity.OrderRecord;
import com.daxueyuan.form.OrderForm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
