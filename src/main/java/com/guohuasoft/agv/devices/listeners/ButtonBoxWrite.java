package com.guohuasoft.agv.devices.listeners;

import com.guohuasoft.agv.devices.DeviceManager;
import com.guohuasoft.agv.devices.model.ButtonBoxModel;
import com.guohuasoft.agv.devices.model.CallButtonModel;
import com.guohuasoft.agv.devices.model.TaskModel;
import com.guohuasoft.agv.devices.services.CallButtonService;
import com.guohuasoft.base.misc.Tracker;
import com.guohuasoft.communication.modbusTcp.ModbusTcp;
import com.serotonin.modbus4j.ModbusMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * 按钮盒子写入监听
 *
 * @author linyehai
 */
public class ButtonBoxWrite implements Runnable {
    private final CallButtonService callButtonService;

    @Autowired
    public ButtonBoxWrite(CallButtonService callButtonService) {
        this.callButtonService = callButtonService;
    }

    @Override
    public void run() {
        boolean runState = true;
        while (runState) {
            List<ButtonBoxModel> buttonBoxes = DeviceManager.getInstance().getButtonBoxModels(); // 获取所有按钮盒子
            try {
                buttonBoxes.forEach(buttonBoxModel -> {
                    // 未连接则创建连接,已连接则执行写入
                    ModbusMaster currentMaster = buttonBoxModel.getModbusMaster();
                    synchronized (buttonBoxModel.getCallTaskModels()) {
                        List<TaskModel> taskModels = buttonBoxModel.getCallTaskModels();
                        if (null != currentMaster) {
                            if (!CollectionUtils.isEmpty(taskModels)) {
                                taskModels.forEach(taskModel -> {
                                    CallButtonModel callButtonModel = buttonBoxModel
                                            .getCallButtonModelByButtonCode(taskModel.getButtonNo());
                                    String resultString = callButtonService.callMaterialByButtonBox(callButtonModel);
                                    boolean executeSuccess = "success".equalsIgnoreCase(resultString);
                                    Tracker.agv(String.format("resultString:%s", resultString));
                                    if (executeSuccess) {
                                        Tracker.agv(String.format("移除前的taskModels：%s", taskModels.toString()));
                                        Tracker.agv(String.format("要移除的task：%s", taskModel.toString()));
                                        // 执行成功则移除任务
                                        taskModels.remove(taskModel);
                                        Tracker.agv(String.format("移除后的taskModels：%s", taskModels.toString()));
                                    } else {
                                        // 执行失败，则闪烁三下后删除任务
                                        try {
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), true);
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), false);
                                            Thread.sleep(5);
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), true);
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), false);
                                            Thread.sleep(5);
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), true);
                                            ModbusTcp.writeCoil(currentMaster, 254,
                                                    Integer.valueOf(callButtonModel.getButtonCode()), false);
                                        } catch (InterruptedException e) {
                                            Tracker.error(e.getCause());
                                            Tracker.agv(String.format("[执行闪烁失败] %s", e.getCause()));
                                        }
                                        Tracker.agv(String.format("失败时：移除前的taskModels：%s", taskModels.toString()));
                                        Tracker.agv(String.format("失败时：要移除的task：%s", taskModel.toString()));
                                        taskModels.remove(taskModel);
                                        Tracker.agv(String.format("失败时：移除后的taskModels：%s", taskModels.toString()));
                                    }
                                });
                            }
                        } else {
                            Tracker.error(String.format("[连接中断的] IP： %s , deviceKey： %s", buttonBoxModel.getIpAddress(), buttonBoxModel.getDeviceKey()));
                            Tracker.agv(String.format("[连接中断的] IP： %s , deviceKey： %s", buttonBoxModel.getIpAddress(), buttonBoxModel.getDeviceKey()));
                        }
                    }
                });
            } catch (Exception e) {
                Tracker.error(e.getCause());
                Tracker.error("========ButtonBoxWrite==========");
                Tracker.agv(String.format("[执行呼叫操作时出错] 出错原因：%s", e.getCause()));
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加灭灯任务
     *
     * @param buttonBoxModel 按钮盒子对象
     * @param taskModel      呼叫任务对象
     */
    public void addLightOffTask(ButtonBoxModel buttonBoxModel, TaskModel taskModel) {
        TaskModel lightOff = new TaskModel(taskModel.getButtonNo(), taskModel.getIpAddress(), taskModel.getDeviceKey(),
                System.currentTimeMillis());
        synchronized (buttonBoxModel.getLightOffTaskModels()) {
            if (buttonBoxModel.getLightOffTaskModels().contains(lightOff)) {
                Tracker.error("不添加灭灯任务");
            } else {
                buttonBoxModel.getLightOffTaskModels().add(lightOff);
                Tracker.info("添加一个灭灯任务");
            }
        }
    }
}
