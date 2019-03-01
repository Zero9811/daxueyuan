package com.daxueyuan.daxueyuan.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:19
 */
@Data
public class OrderForm {
    private String creatorAccount;
    @NotEmpty(message = "快递信息必填")
    private String message;
    //取货地
    @NotEmpty(message = "取货地信息必填")
    private String fromAddress;
    //送货地
    @NotEmpty(message = "送货地信息必填")
    private String toAddress;
    @NotEmpty(message = "包裹名必填")
    private String packageName;
    @NotEmpty(message = "取货码必填")
    private String packageCode;
    @NotEmpty(message = "包裹类型")
    private String packageType;
    @NotEmpty(message = "悬赏金必填")
    private double packagePrice;
    @NotEmpty(message = "收货时间必填")
    private Date receiveTime;
    private String remark;
}
