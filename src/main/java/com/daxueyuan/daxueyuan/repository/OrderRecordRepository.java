package com.daxueyuan.daxueyuan.repository;

import com.daxueyuan.daxueyuan.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:31
 */
public interface OrderRecordRepository extends JpaRepository<OrderRecord,Long> {
    List<OrderRecord> findByCreatorAccount(String creatorAccount);
    List<OrderRecord> findByReceiverAccount(String receiverAccount);
    @Query(value = "from OrderRecord o where o.receiverAccount = null and o.isCancel = false")
    List<OrderRecord> findAccessableOrder();
    //查询所有未完成的订单
    @Query(value = "from OrderRecord o where o.creatorAccount = ?0 and o.orderState < ?1 and o.isCancel = false")
    List<OrderRecord> findCreatorNowOrders(String creatorAccount,int completeState);
    //查询所有已完成的订单
    @Query(value = "from OrderRecord o where o.creatorAccount = ?0 and o.orderState = ?1 and o.isCancel = false")
    List<OrderRecord> findCreatorCompleteOrders(String creatorAccount,int completeState);
    @Query(value = "from OrderRecord o where o.receiverAccount = ?0 and o.orderState < ?1 and o.isCancel = false")
    List<OrderRecord> findReceiverNowOrders(String receiverAccount,int completeState);
    @Query(value = "from OrderRecord o where o.receiverAccount = ?0 and o.orderState = ?1 and o.isCancel = false")
    List<OrderRecord> findReceiverCompleteOrders(String receiverAccount,int completeState);
    //查询某个状态的自己的所有订单
    @Query(value = "from OrderRecord o where o.creatorAccount = ?0 and o.orderState = ?1 and o.isCancel = false")
    List<OrderRecord> findCreatorStateOrders(String creatorAccount,int orderState);
    @Query(value = "from OrderRecord o where o.receiverAccount = ?0 and o.orderState = ?1 and o.isCancel = false")
    List<OrderRecord> findReceiverStateOrders(String receiverAccount,int orderState);
    @Query(value = "from OrderRecord o where (o.creatorAccount = ?0 and o.orderState = ?1 and o.isCancel = false) or (o.receiverAccount = ?0 and o.orderState = ?1 and o.isCancel = false)")
    List<OrderRecord> findCreatorAndReceiverStateOrders(String account,int orderState);
}
