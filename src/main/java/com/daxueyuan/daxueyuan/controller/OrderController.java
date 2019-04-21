package com.daxueyuan.daxueyuan.controller;

import com.daxueyuan.daxueyuan.VO.ResultVO;
import com.daxueyuan.daxueyuan.converter.OrderForm2OrderRecord;
import com.daxueyuan.daxueyuan.entity.OrderRecord;
import com.daxueyuan.daxueyuan.exception.OrderException;
import com.daxueyuan.daxueyuan.form.OrderForm;
import com.daxueyuan.daxueyuan.nums.OrderStateEnum;
import com.daxueyuan.daxueyuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
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
        //orderId暂时不用设置，数据库自增
        orderRecord.setCreateTime(new Date());
        orderRecord.setOrderState(OrderStateEnum.FREE.getCode());
        orderRecord.setIsCancel(false);
        orderService.save(orderRecord);
        ResultVO resultVO = successResult(null);
        return resultVO;
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
        ResultVO resultVO = successResult(orderRecord);
        return resultVO;
    }

    /**
     * 获取当前所有还未被接单的订单
     * @return
     */
    @GetMapping("/mart")
    public ResultVO findAllAccess(){
        List result = orderService.findAllAccessableOrder();
        if (result == null || result.size()<1){
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
    }

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
        orderRecord.setCancelReason(cancelReason);
        orderService.save(orderRecord);
        return successResult("订单已取消");
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
        return successResult("已退单");
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
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
        List result = orderService.findCreatorStateOrders(creatorAccount,orderState);
        if (result == null || result.size()<1){
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
            ResultVO resultVO = new ResultVO();
            resultVO.setCode(13);
            resultVO.setMsg("数据为空");
            resultVO.setData(result);
            return resultVO;
        }
        return successResult(result);
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
        orderRecord.setOrderState(state);
        orderService.save(orderRecord);
        return successResult(orderRecord);
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
        return successResult(orderRecord);
    }

    private ResultVO successResult(Object o){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(12);
        resultVO.setMsg("操作成功");
        resultVO.setData(o);
        return resultVO;
    }
}
