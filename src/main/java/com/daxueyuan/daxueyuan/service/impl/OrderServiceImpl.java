package com.daxueyuan.daxueyuan.service.impl;

import com.daxueyuan.daxueyuan.entity.OrderRecord;
import com.daxueyuan.daxueyuan.nums.OrderStateEnum;
import com.daxueyuan.daxueyuan.repository.OrderRecordRepository;
import com.daxueyuan.daxueyuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 12:08
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRecordRepository orderRecordRepository;

    @Override
    public void save(OrderRecord orderRecord) {
        orderRecordRepository.save(orderRecord);
    }

    @Override
    public List<OrderRecord> findByCreatorAccount(String creatorAccount) {
        return orderRecordRepository.findByCreatorAccount(creatorAccount);
    }

    @Override
    public List<OrderRecord> findByReceiverAccount(String receiverAccount) {
        return orderRecordRepository.findByReceiverAccount(receiverAccount);
    }

    /**
     * 查找订单创建后在48小时有效期内的订单
     * @return
     */
    @Override
    public List<OrderRecord> findAllAccessableOrder() {
        List<OrderRecord> orderRecords = orderRecordRepository.findAccessableOrder();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        now = calendar.getTime();
        log.info("time is {}",now);
        Iterator<OrderRecord> iterator = orderRecords.iterator();
        while (iterator.hasNext()){
            OrderRecord orderRecord = iterator.next();
            if (orderRecord.getCreateTime().before(now))
                iterator.remove();
        }
        return orderRecords;
    }

    @Override
    public OrderRecord findById(long orderId) {
        return orderRecordRepository.getOne(orderId);
    }

    @Override
    public List<OrderRecord> findCreatorNowOrders(String creatorAccount) {
        return orderRecordRepository.findCreatorNowOrders(creatorAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findCreatorCompleteOrders(String creatorAccount) {
        return orderRecordRepository.findCreatorCompleteOrders(creatorAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findReceiverNowOrders(String receiverAccount) {
        return orderRecordRepository.findReceiverNowOrders(receiverAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findReceiverCompleteOrders(String receiverAccount) {
        return orderRecordRepository.findReceiverCompleteOrders(receiverAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findCreatorStateOrders(String creatorAccount, int orderState) {
        return orderRecordRepository.findCreatorStateOrders(creatorAccount,orderState);
    }

    @Override
    public List<OrderRecord> findReceiverStateOrders(String receiverAccount, int orderState) {
        return orderRecordRepository.findReceiverStateOrders(receiverAccount,orderState);
    }

    @Override
    public void saveAll(List<OrderRecord> orderRecordList) {
        orderRecordRepository.saveAll(orderRecordList);
    }
}
