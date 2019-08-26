package com.daxueyuan.service;

import com.daxueyuan.entity.EventTracking;

import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/8/26 21:55
 */
public interface EventTrackingService {
    void save(EventTracking eventTracking);
    EventTracking findById(long id);
    List<EventTracking> findAll();
}
