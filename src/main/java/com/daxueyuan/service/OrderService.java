package com.daxueyuan.service;

import com.daxueyuan.entity.OrderRecord;

import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 12:03
 */
public interface OrderService {
    void save(OrderRecord orderRecord);
    List<OrderRecord> findByCreatorAccount(String creatorAccount);
    List<OrderRecord> findByReceiverAccount(String receiverAccount);
    List<OrderRecord> findAllAccessableOrder(String latS,String lngS,String distance);
    OrderRecord findById(long orderId);
    List<OrderRecord> findCreatorNowOrders(String creatorAccount);
    List<OrderRecord> findCreatorCompleteOrders(String creatorAccount);
    List<OrderRecord> findReceiverNowOrders(String receiverAccount);
    List<OrderRecord> findReceiverCompleteOrders(String receiverAccount);
    List<OrderRecord> findCreatorStateOrders(String creatorAccount,int orderState);
    List<OrderRecord> findReceiverStateOrders(String receiverAccount,int orderState);
    void saveAll(List<OrderRecord> orderRecordList);
    List<OrderRecord> findCreatorAndReceiverStateOrders(String account,int state);
    List<OrderRecord> findCancelOrders(String account);
}
