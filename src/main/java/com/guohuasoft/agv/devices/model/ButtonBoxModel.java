package com.guohuasoft.agv.devices.model;

import com.serotonin.modbus4j.ModbusMaster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 按钮盒子前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButtonBoxModel implements Serializable {
    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 端口号
     */
    private int port;

    /**
     * 设备唯一识别码
     */
    private String deviceKey;

    /**
     * ModBus主站对象
     */
    private ModbusMaster modbusMaster;

    /**
     * 按钮列表
     */
    private List<CallButtonModel> callButtonModels;

    /**
     * 叫料状态  对写加锁，读不加锁
     */
    private CopyOnWriteArrayList<Boolean> callState;

    /**
     * 呼叫任务列表 呼叫包括叫料以及退货架
     */
    private List<TaskModel> callTaskModels;

    /**
     * 灭灯任务列表
     */
    private CopyOnWriteArrayList<TaskModel> lightOffTaskModels;

    public ButtonBoxModel(CallButtonModel callButtonModel) {
        this.ipAddress = callButtonModel.getIpAddress();
        this.port = callButtonModel.getPort();
        this.modbusMaster = null;
        this.callButtonModels = new ArrayList<>();
        this.callButtonModels.add(callButtonModel);
        this.callState = new CopyOnWriteArrayList<>();
        this.callTaskModels = new CopyOnWriteArrayList<>();
        this.lightOffTaskModels = new CopyOnWriteArrayList<>();
        callState.add(false);
    }

    /**
     * 通过按钮编号获取叫料按钮对象
     *
     * @param buttonCode 按钮编号
     * @return 按钮对象
     */
    public CallButtonModel getCallButtonModelByButtonCode(int buttonCode) {
        Optional<CallButtonModel> callButtonModelOptional = this.callButtonModels.stream().filter(item -> item.getButtonCode().equals(String.valueOf(buttonCode))).findFirst();
        if (!callButtonModelOptional.isPresent()) {
            return null;
        } else {
            return callButtonModelOptional.get();
        }
    }
}
