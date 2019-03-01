package com.daxueyuan.daxueyuan.nums;

import lombok.Getter;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:14
 */
@Getter
public enum OrderStateEnum {
    CANCEL(0,"取消"),
    FREE(1,"未接单"),
    BOOK(2,"已接单"),
    PICK(3,"已取货"),
    RECEIVE(4,"已收货"),
    FINISH(5,"已完成"),
    ;
    private int code;
    private String msg;

    OrderStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
