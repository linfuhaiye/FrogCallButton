package com.guohuasoft.agv.devices.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 基本的请求对象
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
public class BaseRequestModel {
    /**
     * ip地址：非必填
     */
    private String ipAddress;

    /**
     * 设备唯一码
     */
    private String deviceKey;

    /**
     * 编码格式
     */
    private String codingFormat;

    /**
     * 按钮编号
     */
    private String buttonCode;

    /**
     * 转化为Get请求参数模式
     *
     * @return get请求参数
     */
    public String toParameter() {
        StringBuffer parameters = new StringBuffer();
        parameters.append("ipAddress=");
        parameters.append(ipAddress);
        parameters.append("&deviceKey=");
        parameters.append(deviceKey);
        parameters.append("&codingFormat=");
        parameters.append(codingFormat);
        parameters.append("&buttonCode=");
        parameters.append(buttonCode);
        return parameters.toString();
    }
}
