package com.daxueyuan.controller;

import com.daxueyuan.VO.ResultVO;
import com.daxueyuan.converter.OrderForm2OrderRecord;
import com.daxueyuan.entity.OrderRecord;
import com.daxueyuan.exception.OrderException;
import com.daxueyuan.form.OrderForm;
import com.daxueyuan.nums.OrderStateEnum;
import com.daxueyuan.service.OrderService;
import com.daxueyuan.util.CompareOrderDateUtil;
import com.daxueyuan.util.PushUtil;
import com.daxueyuan.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Security;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:24
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderForm2OrderRecord orderForm2OrderRecord;
    /**
     * 创建订单接口
     * orderForm的内容为订单的内容
     * binding用于校验，无需传参
     * 1.拼接orderRecord
     * 2.保存
     * 3.返回创建成功
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult) throws ParseException {
        if(bindingResult.hasErrors()){
            log.info("参数不正确，orderForm={}",orderForm);
            throw new OrderException(1,bindingResult.getFieldError().getDefaultMessage());
        }
        OrderRecord orderRecord = orderForm2OrderRecord.convert(orderForm);
        try {
            orderRecord.setLat(Double.valueOf(orderForm.getLatS()));
            orderRecord.setLng(Double.valueOf(orderForm.getLngS()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //orderId暂时不用设置，数据库自增
        orderRecord.setCreateTime(new Date());
        orderRecord.setOrderState(OrderStateEnum.FREE.getCode());
        orderRecord.setIsCancel(false);
        orderService.save(orderRecord);
        return ResultVOUtil.returnResult(12,"操作成功", null);
    }

    /**
     * 接单人接单接口
     * 1.查询订单
     * 2.添加接单人
     * 3.改变订单状态
     * 4.保存并返回
     * @param orderIdS
     * @param receiverAccount
     * @return
     */
    @PutMapping("/receive")
    public ResultVO receiveOrder(String orderIdS,String receiverAccount){
        long orderId = Long.valueOf(orderIdS);
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setReceiverAccount(receiverAccount);
        orderRecord.setOrderState(OrderStateEnum.BOOK.getCode());
        orderService.save(orderRecord);

        String alias = orderRecord.getCreatorAccount();
        alias = DigestUtils.md5DigestAsHex(alias.getBytes());
        PushUtil.sendPush(alias, "订单已被接收");
        return ResultVOUtil.returnResult(12,"操作成功", orderRecord);
    }

    /**
     * 获取当前所有还未被接单的订单
     * @return
     */
    @GetMapping("/mart")
    public ResultVO findAllAccess(String latS,String lngS,String distance){
        List result = orderService.findAllAccessableOrder(latS,lngS,distance);

        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }
//    new Comparator<OrderRecord>() {
//        public int compare(OrderRecord arg0,OrderRecord arg1){
//            Date data1 = arg0.getCreateTime();
//            Date date2 = arg1.getCreateTime();
//            if (data1.before(date2))
//                return 1;
//            else if (data1.after(date2))
//                return -1;
//            else
//                return 0;
//        }
//    }

    /**
     * 用户自己取消订单
     * @param orderIdS
     * @param cancelReason
     * @return
     */
    @PutMapping("/creatorCancel")
    public ResultVO cancelOrder(String orderIdS,String cancelReason){
        long orderId = Long.valueOf(orderIdS);
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setIsCancel(true);
        orderRecord.setOrderState(OrderStateEnum.CANCEL.getCode());
        orderRecord.setCancelReason(cancelReason);
        orderService.save(orderRecord);
        return ResultVOUtil.returnResult(12,"操作成功", "订单已经取消");
    }

    /**
     * 取快递的人退单
     * @param orderIdS
     * @return
     */
    @PutMapping("/receiverCancel")
    public ResultVO receiverCancel(String orderIdS){
        long orderId = Long.valueOf(orderIdS);
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setReceiverAccount(null);
        orderService.save(orderRecord);
        return ResultVOUtil.returnResult(12,"操作成功", "已退单");
    }

    @GetMapping("/cancelled")
    public ResultVO showCancelOrders(String account) {
        List<OrderRecord> result = orderService.findCancelOrders(account);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 用户查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/creatorNowOrders")
    public ResultVO creatorNowOrders(String creatorAccount){
        List result = orderService.findCreatorNowOrders(creatorAccount);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 用户查看自己所有已完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/creatorCompleteOrders")
    public ResultVO creatorCompleteOrders(String creatorAccount){
        List result = orderService.findCreatorCompleteOrders(creatorAccount);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 用户按照订单状态查找某一状态的自己的所有的订单
     * @param creatorAccount
     * @param orderStateS
     * @return
     */
    @GetMapping("/creatorStateOrders")
    public ResultVO creatorStateOrders(String creatorAccount,String orderStateS){
        int orderState = Integer.valueOf(orderStateS);
        log.info("查询状态 ： "+orderState);
        System.out.println("状态: "+orderState);
        List result = orderService.findCreatorStateOrders(creatorAccount,orderState);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 接单人查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/receiverNowOrders")
    public ResultVO receiverNowOrders(String creatorAccount){
        List result = orderService.findReceiverNowOrders(creatorAccount);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 接单人查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/receiverCompleteOrders")
    public ResultVO receiverCompleteOrders(String creatorAccount){
        List result = orderService.findReceiverCompleteOrders(creatorAccount);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    /**
     * 接单人按照订单状态查看自己某一订单状态下的所有订单
     * @param receiverAccount
     * @param orderStateS
     * @return
     */
    @GetMapping("/receiverStateOrders")
    public ResultVO receiverStateOrders(String receiverAccount,String orderStateS){
        int orderState = Integer.valueOf(orderStateS);
        List result = orderService.findReceiverStateOrders(receiverAccount,orderState);
        if (result == null || result.size()<1){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        Collections.sort(result, new CompareOrderDateUtil());
        return ResultVOUtil.returnResult(12,"操作成功", result);
    }

    @GetMapping("/creatorAndReceiverStateOrders")
    public ResultVO creatorAndReceiverStateOrders(String account,String orderStateS){
        int orderState = Integer.valueOf(orderStateS);
        List<OrderRecord> result = orderService.findCreatorAndReceiverStateOrders(account,orderState);

        if (result.size()==0){
            return ResultVOUtil.returnResult(13,"数据为空", result);
        }
        else {
            Collections.sort(result, new CompareOrderDateUtil());
            return ResultVOUtil.returnResult(12,"操作成功", result);
        }
    }

    /**
     * 改变订单状态
     * @param orderIdS
     * @param stateS
     * @return
     */
    @PutMapping("/orderState")
    public ResultVO changeOrderState(String orderIdS,String stateS){
        int state = Integer.valueOf(stateS);
        long orderId = Long.valueOf(orderIdS);
        //订单状态向前改变的校验暂时不考虑
        OrderRecord orderRecord = orderService.findById(orderId);
        log.info("订单状态: "+orderRecord.getOrderState());
        orderRecord.setOrderState(state);
        orderService.save(orderRecord);
        log.info("订单id: "+orderRecord.getOrderId());
        return ResultVOUtil.returnResult(12,"操作成功", orderRecord);
    }

    /**
     * 订单还未被接单时，用户可以修改订单信息
     * @param orderIdS
     * @param orderForm
     * @param bindingResult
     * @return
     */
    //TODO 这个接口可能有问题
    @PutMapping("/orderInfo")
    public ResultVO changeOrderInfo(String orderIdS,@Valid OrderForm orderForm,BindingResult bindingResult){
        long orderId = Long.valueOf(orderIdS);
        if(bindingResult.hasErrors()){
            log.info("参数不正确，orderForm={}",orderForm);
            throw new OrderException(1,bindingResult.getFieldError().getDefaultMessage());
        }
        OrderRecord orderRecord = orderService.findById(orderId);
        if(orderRecord.getOrderState() != OrderStateEnum.FREE.getCode())
            throw new OrderException(2,"当前订单状态不能修改订单信息");
        BeanUtils.copyProperties(orderForm,orderRecord);
        log.info("订单Id为 + "+orderRecord.getOrderId());
        orderService.save(orderRecord);
        return ResultVOUtil.returnResult(12,"操作成功", orderRecord);
    }
}
