package com.daxueyuan.daxueyuan.VO;

import lombok.Data;

/**
 * @Author: Sean
 * @Date: 2019/2/28 23:34
 */
@Data
public class ResultVO<T> {

    private int code;
    private String msg;
    private T data;
}
