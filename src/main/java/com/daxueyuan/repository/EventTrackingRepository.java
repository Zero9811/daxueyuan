package com.daxueyuan.repository;

import com.daxueyuan.entity.EventTracking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Sean
 * @Date: 2019/8/26 21:51
 */
public interface EventTrackingRepository extends JpaRepository<EventTracking, Long> {
}
