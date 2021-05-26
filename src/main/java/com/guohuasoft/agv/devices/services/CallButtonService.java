package com.guohuasoft.agv.devices.services;

import com.guohuasoft.agv.devices.entities.CallButton;
import com.guohuasoft.agv.devices.mappers.CallButtonDao;
import com.guohuasoft.agv.devices.model.BaseRequestModel;
import com.guohuasoft.agv.devices.model.ButtonBoxModel;
import com.guohuasoft.agv.devices.model.CallButtonModel;
import com.guohuasoft.agv.devices.model.CallResponeModel;
import com.guohuasoft.base.misc.HttpUtils;
import com.guohuasoft.base.misc.Tracker;
import com.guohuasoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按钮设备服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CallButtonService extends BaseService<CallButtonDao, CallButton> {
    private final CallButtonDao callButtonDao;
    @Value("${button-box.url}")
    private String url;
    @Value("${button-box.coding_format}")
    private String codingFormat;

    @Autowired
    public CallButtonService(CallButtonDao callButtonDao) {
        super(callButtonDao);
        this.callButtonDao = callButtonDao;
    }

    /**
     * 通过主键获取按钮设备详情
     *
     * @param id 按钮设备ID
     * @return
     */
    public CallButtonModel selectCallButtonById(Long id) {
        return callButtonDao.selectCallButtonById(id);
    }

    /**
     * 获取所有按钮设备详情
     * @return
     */
    public List<CallButtonModel> selectCallButtons() {
        return callButtonDao.selectCallButtons();
    }

    /**
     * 查找按钮盒子
     *
     * @return 按钮盒子列表
     */
    public List<ButtonBoxModel> selectButtonBoxes() {
        List<CallButtonModel> callButtonModels = callButtonDao.selectConnectByModBusCallButtons();
        Map<String, ButtonBoxModel> buttonBoxModelMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(callButtonModels)) {
            callButtonModels.forEach(callButtonModel -> {
                ButtonBoxModel buttonBoxModel = buttonBoxModelMap.get(callButtonModel.getIpAddress());
                if (!ObjectUtils.isEmpty(buttonBoxModel)) {
                    buttonBoxModel.getCallButtonModels().add(callButtonModel);
                    buttonBoxModel.getCallState().add(false);
                } else {
                    buttonBoxModel = new ButtonBoxModel(callButtonModel);
                    buttonBoxModelMap.put(callButtonModel.getIpAddress(), buttonBoxModel);
                }
            });
        } else {
            return null;
        }
        List<ButtonBoxModel> backButtonBoxes = new ArrayList<>();
        buttonBoxModelMap.forEach((key, value) -> {
            backButtonBoxes.add(value);
        });
        return backButtonBoxes;
    }

    /**
     * 通过按钮盒子叫料
     *
     * @param callButtonModel 按钮盒子对象
     * @return 叫料返回结果
     */
    public String callMaterialByButtonBox(CallButtonModel callButtonModel) {
        Tracker.agv(String.format("按钮盒子：%s 发起请求：%s",callButtonModel.toString(), url+"/callMaterials/button"));
        BaseRequestModel baseRequestModel = new BaseRequestModel(callButtonModel.getIpAddress(), callButtonModel.getDeviceKey(), codingFormat, callButtonModel.getButtonCode());
        CallResponeModel callResponeModel = HttpUtils.getJson(url+"/callMaterials/button", null, baseRequestModel.toParameter(), CallResponeModel.class);
        Tracker.agv(String.format("按钮请求返回结果：%s", callResponeModel.getMessage()));
        return callResponeModel.getMessage();
    }

    public String callTest(long id) {
        CallButtonModel callButtonModel = callButtonDao.selectCallButtonById(id);
        return callMaterialByButtonBox(callButtonModel);
    }
}
