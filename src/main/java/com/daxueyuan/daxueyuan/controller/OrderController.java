package com.daxueyuan.daxueyuan.controller;

import com.daxueyuan.daxueyuan.VO.ResultVO;
import com.daxueyuan.daxueyuan.entity.OrderRecord;
import com.daxueyuan.daxueyuan.exception.OrderException;
import com.daxueyuan.daxueyuan.form.OrderForm;
import com.daxueyuan.daxueyuan.nums.OrderStateEnum;
import com.daxueyuan.daxueyuan.service.OrderService;
import com.daxueyuan.daxueyuan.util.ResultVOUtil;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @Author: Sean
 * @Date: 2019/3/1 10:24
 */
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

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
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("参数不正确，orderForm={}",orderForm);
            throw new OrderException(1,bindingResult.getFieldError().getDefaultMessage());
        }
        OrderRecord orderRecord = new OrderRecord();
        BeanUtils.copyProperties(orderForm,orderRecord);
        //orderId暂时不用设置，数据库自增
        orderRecord.setCreateTime(new Date());
        orderRecord.setOrderState(OrderStateEnum.FREE.getCode());
        orderRecord.setIsCancel(false);
        orderService.save(orderRecord);
        return ResultVOUtil.success("创建成功");
    }

    /**
     * 接单人接单接口
     * 1.查询订单
     * 2.添加接单人
     * 3.改变订单状态
     * 4.保存并返回
     * @param orderId
     * @param receiverAccount
     * @return
     */
    @PutMapping("/receive")
    public ResultVO receiveOrder(long orderId,String receiverAccount){
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setReceiverAccount(receiverAccount);
        orderRecord.setOrderState(OrderStateEnum.BOOK.getCode());
        orderService.save(orderRecord);
        return ResultVOUtil.success(orderRecord);
    }

    /**
     * 获取当前所有还未被接单的订单
     * @return
     */
    @GetMapping("/mart")
    public ResultVO findAllAccess(){
        List result = orderService.findAllAccessableOrder();
        return ResultVOUtil.success(result);
    }

    /**
     * 用户自己取消订单
     * @param orderId
     * @param cancelReason
     * @return
     */
    @GetMapping("/creatorCancel")
    public ResultVO cancelOrder(long orderId,String cancelReason){
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setIsCancel(true);
        orderRecord.setCancelReason(cancelReason);
        orderService.save(orderRecord);
        return ResultVOUtil.success("订单已取消");
    }

    /**
     * 取快递的人退单
     * @param orderId
     * @return
     */
    @GetMapping("/receiverCancel")
    public ResultVO receiverCancel(long orderId){
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setReceiverAccount(null);
        orderService.save(orderRecord);
        return ResultVOUtil.success("已退单");
    }

    /**
     * 用户查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/creatorNowOrders")
    public ResultVO creatorNowOrders(String creatorAccount){
        List result = orderService.findCreatorNowOrders(creatorAccount);
        return ResultVOUtil.success(result);
    }

    /**
     * 用户查看自己所有已完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/creatorCompleteOrders")
    public ResultVO creatorCompleteOrders(String creatorAccount){
        List result = orderService.findCreatorCompleteOrders(creatorAccount);
        return ResultVOUtil.success(result);
    }

    /**
     * 用户按照订单状态查找某一状态的自己的所有的订单
     * @param creatorAccount
     * @param orderState
     * @return
     */
    @GetMapping("/creatorStateOrders")
    public ResultVO creatorStateOrders(String creatorAccount,int orderState){
        List list = orderService.findCreatorStateOrders(creatorAccount,orderState);
        return ResultVOUtil.success(list);
    }

    /**
     * 接单人查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/receiverNowOrders")
    public ResultVO receiverNowOrders(String creatorAccount){
        return ResultVOUtil.success(orderService.findReceiverNowOrders(creatorAccount));
    }

    /**
     * 接单人查看自己所有未完成的订单
     * @param creatorAccount
     * @return
     */
    @GetMapping("/receiverCompleteOrders")
    public ResultVO receiverCompleteOrders(String creatorAccount){
        return ResultVOUtil.success(orderService.findReceiverCompleteOrders(creatorAccount));
    }

    /**
     * 接单人按照订单状态查看自己某一订单状态下的所有订单
     * @param receiverAccount
     * @param orderState
     * @return
     */
    @GetMapping("/receiverStateOrders")
    public ResultVO receiverStateOrders(String receiverAccount,int orderState){
        List list = orderService.findReceiverStateOrders(receiverAccount,orderState);
        return ResultVOUtil.success(list);
    }

    /**
     * 改变订单状态
     * @param orderId
     * @param state
     * @return
     */
    @PutMapping("/orderState")
    public ResultVO changeOrderState(long orderId,int state){
        //订单状态向前改变的校验暂时不考虑
        OrderRecord orderRecord = orderService.findById(orderId);
        orderRecord.setOrderState(state);
        orderService.save(orderRecord);
        return ResultVOUtil.success(orderRecord);
    }

    /**
     * 订单还未被接单时，用户可以修改订单信息
     * @param orderId
     * @param orderForm
     * @param bindingResult
     * @return
     */
    //TODO 这个接口可能有问题
    @PutMapping("/orderInfo")
    public ResultVO changeOrderInfo(long orderId,@Valid OrderForm orderForm,BindingResult bindingResult){
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
        return ResultVOUtil.success("订单信息已被修改");
    }
}
