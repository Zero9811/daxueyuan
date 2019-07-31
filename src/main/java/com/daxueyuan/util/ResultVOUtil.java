package com.daxueyuan.util;


import com.daxueyuan.VO.ResultVO;
import com.daxueyuan.nums.ResultVOCodeEnum;

/**
 * @Author: Sean
 * @Date: 2019/1/4 17:30
 */
public class ResultVOUtil {
    //TODO 单例模式的改造
    private static ResultVOUtil resultVOUtil;
    private static ResultVO resultVO;

    private ResultVOUtil() {

    }

    public static ResultVO returnResult(int code, String msg, Object data) {
        if (resultVOUtil == null) {
            resultVOUtil = new ResultVOUtil();
        }
        if (resultVO == null) {
            resultVO = new ResultVO();
        }
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(data);

        return resultVO;
    }
}
