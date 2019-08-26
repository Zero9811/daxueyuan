package com.daxueyuan.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/8/26 19:51
 */
@Entity
@Data
@Table(name = "eventTracking_table")
public class EventTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;
    private String account;
    private String appVersion;
    private String eventType;
    private String eventContent;
    private Date eventTime;
}
