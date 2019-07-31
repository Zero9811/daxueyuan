package com.daxueyuan.exception;

/**
 * @Author: Sean
 * @Date: 2019/1/8 22:04
 */
public class OrderException extends RuntimeException {
    private int code;
    public OrderException(int code, String msg){
        super(msg);
        this.code = code;
    }
}
