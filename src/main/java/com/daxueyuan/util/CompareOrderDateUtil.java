package com.daxueyuan.util;

import com.daxueyuan.entity.OrderRecord;

import java.util.Comparator;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/8/1 22:16
 */
public class CompareOrderDateUtil implements Comparator<OrderRecord> {
    @Override
    public int compare(OrderRecord arg0, OrderRecord arg1) {
        Date data1 = arg0.getCreateTime();
        Date date2 = arg1.getCreateTime();
        if (data1.before(date2))
            return 1;
        else if (data1.after(date2))
            return -1;
        else
            return 0;
    }
}
