package com.daxueyuan.controller;

import com.daxueyuan.VO.ResultVO;
import com.daxueyuan.entity.EventTracking;
import com.daxueyuan.nums.ResultVOCodeEnum;
import com.daxueyuan.service.EventTrackingService;
import com.daxueyuan.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Sean
 * @Date: 2019/8/26 22:02
 */
@Controller
@Slf4j
@RequestMapping("/event")
public class EventTrackingController {

    @Autowired
    private EventTrackingService eventTrackingService;

    @PostMapping("/create")
    public ResultVO create(EventTracking eventTracking) {
        eventTrackingService.save(eventTracking);
        return ResultVOUtil.returnResult(12, "操作成功", "操作成功");
    }
}
