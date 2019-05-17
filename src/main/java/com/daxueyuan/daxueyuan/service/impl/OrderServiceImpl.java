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
    //TODO 查询方式待优化
    @Override
    public List<OrderRecord> findAllAccessableOrder(String latS,String lngS,String distanceS) {
        List<OrderRecord> orderRecords = orderRecordRepository.findAccessableOrder();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        now = calendar.getTime();
        log.info("time is {}",now);
        Iterator<OrderRecord> it = orderRecords.iterator();
        double lat = Double.valueOf(latS);
        double lng = Double.valueOf(lngS);
        double distance = Double.valueOf(distanceS);
        while (it.hasNext()){
            OrderRecord orderRecord = it.next();
            double tempDis = getDist(orderRecord.getLng(),orderRecord.getLat(),lng,lat);
            if (orderRecord.getCreateTime().before(now))
                it.remove();
            else if (tempDis > distance)
                it.remove();
            else
                orderRecord.setDistance(tempDis);
        }
        return orderRecords;
    }

    @Override
    public OrderRecord findById(long orderId) {
        return orderRecordRepository.getOne(orderId);
    }

    @Override
    public List<OrderRecord> findCreatorNowOrders(String creatorAccount) {
        return orderRecordRepository.findByCreatorAccountAndOrderStateLessThan(creatorAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findCreatorCompleteOrders(String creatorAccount) {
        return orderRecordRepository.findByCreatorAccountAndAndOrderStateAndIsCancelFalse(creatorAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findReceiverNowOrders(String receiverAccount) {
        return orderRecordRepository.findByReceiverAccountAndOrderStateLessThan(receiverAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findReceiverCompleteOrders(String receiverAccount) {
        return orderRecordRepository.findByReceiverAccountAndOrderStateAndIsCancelFalse(receiverAccount, OrderStateEnum.FINISH.getCode());
    }

    @Override
    public List<OrderRecord> findCreatorStateOrders(String creatorAccount, int orderState) {
        return orderRecordRepository.
                findByCreatorAccountAndAndOrderStateAndIsCancelFalse(creatorAccount,orderState);
    }

    @Override
    public List<OrderRecord> findReceiverStateOrders(String receiverAccount, int orderState) {
        return orderRecordRepository.findByReceiverAccountAndOrderStateAndIsCancelFalse(receiverAccount,orderState);
    }

    @Override
    public void saveAll(List<OrderRecord> orderRecordList) {
        orderRecordRepository.saveAll(orderRecordList);
    }

    @Override
    public List<OrderRecord> findCreatorAndReceiverStateOrders(String account, int state) {
        List<OrderRecord> result1 = orderRecordRepository.
                findByCreatorAccountAndAndOrderStateAndIsCancelFalse(account,state);
        List<OrderRecord> result2 = orderRecordRepository.
                findByReceiverAccountAndOrderStateAndIsCancelFalse(account,state);
        result1.addAll(result2);
        return result1;
    }

    @Override
    public List<OrderRecord> findCancelOrders(String account) {
        return orderRecordRepository.findByCreatorAccountAndOrderState(account, OrderStateEnum.CANCEL.getCode());
    }

    private final double EARTH_RADIUS = 6378137;

    private double getDist(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double radLon1 = rad(lng1);
        double radLon2 = rad(lng2);
        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    private double rad(double d){
        return d*Math.PI/180.0;
    }
}
