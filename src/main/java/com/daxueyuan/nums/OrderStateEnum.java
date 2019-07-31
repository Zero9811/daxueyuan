package com.daxueyuan.nums;

import lombok.Getter;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:14
 */
@Getter
public enum OrderStateEnum {
    CANCEL(0,"取消"),
    FREE(1,"未接单"),
    APPLYING(2,"申请中"),
    BOOK(3,"已接单"),
    PICK(4,"已取货"),
    RECEIVE(5,"已收货"),
    FINISH(6,"已完成"),
    ;
    private int code;
    private String msg;

    OrderStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
