package com.guohuasoft.agv.devices.controllers;

import com.guohuasoft.agv.devices.services.CallButtonService;
import com.guohuasoft.base.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配送管理控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class CallButtonController {
    private final CallButtonService callButtonService;

    public CallButtonController(CallButtonService callButtonService) {
        this.callButtonService = callButtonService;
    }

    /**
     * 添加配送任务
     *
     * @param id
     * @return 响应内容
     * @throws Exception
     */
    @GetMapping("/call/test")
    public RestResponse addDeliveryTask(long id) {
        return new RestResponse(HttpStatus.OK, null, callButtonService.callTest(id));
    }
}
