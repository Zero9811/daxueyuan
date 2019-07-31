package com.daxueyuan.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/5/14 13:35
 */
@Data
public class OrderRecordOutput implements Serializable {
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
    private String packagePrice;
    private String receiveTime;
    private String remark;
    private String receiverAccount;
    private int orderState;
    private boolean isCancel;
    private String cancelReason;
    //经纬度
    private double lat;
    private double lng;
    private double distance;

    public void setIsCancel(boolean isCancel){
        this.isCancel = isCancel;
    }

    public boolean getIsCancel(){
        return isCancel;
    }
}
