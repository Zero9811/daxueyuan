package com.daxueyuan.daxueyuan.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:02
 */
@Data
@Entity
@Table(name = "orderRecord_table")
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private String creatorAccount;
    private Date createTime;
    private String message;
    //取货地
    private String fromAddress;
    //送货地
    private String toAddress;
    private String packageName;
    private String packageCode;
    private String packageType;
    private double packagePrice;
    private Date receiveTime;
    private String remark;
    private String receiverAccount;
    private int orderState;
    private boolean isCancel;
    private String cancelReason;

    public void setIsCancel(boolean isCancel){
        this.isCancel = isCancel;
    }

    public boolean getIsCancel(){
        return isCancel;
    }
}
