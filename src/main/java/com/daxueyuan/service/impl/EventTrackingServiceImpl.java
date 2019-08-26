package com.daxueyuan.service.impl;

import com.daxueyuan.entity.EventTracking;
import com.daxueyuan.repository.EventTrackingRepository;
import com.daxueyuan.service.EventTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/8/26 21:57
 */
@Service
@Slf4j
public class EventTrackingServiceImpl implements EventTrackingService {

    @Autowired
    private EventTrackingRepository eventTrackingRepository;

    @Override
    public void save(EventTracking eventTracking) {
        log.info("保存一次");
        eventTrackingRepository.save(eventTracking);
    }

    @Override
    public EventTracking findById(long id) {
        return eventTrackingRepository.getOne(id);
    }

    @Override
    public List<EventTracking> findAll() {
        return eventTrackingRepository.findAll();
    }
}
