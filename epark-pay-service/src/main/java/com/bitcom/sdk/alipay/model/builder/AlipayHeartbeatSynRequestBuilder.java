package com.bitcom.sdk.alipay.model.builder;

import com.bitcom.sdk.alipay.model.hb.EquipStatus;
import com.bitcom.sdk.alipay.model.hb.ExceptionInfo;
import com.bitcom.sdk.alipay.model.hb.PosTradeInfo;
import com.bitcom.sdk.alipay.model.hb.Product;
import com.bitcom.sdk.alipay.model.hb.SysTradeInfo;
import com.bitcom.sdk.alipay.model.hb.TradeInfo;
import com.bitcom.sdk.alipay.model.hb.Type;
import com.bitcom.sdk.alipay.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class AlipayHeartbeatSynRequestBuilder extends RequestBuilder {
    private BizContent bizContent = new BizContent();


    public BizContent getBizContent() {
        return this.bizContent;
    }


    public boolean validate() {
        if (this.bizContent.product == null) {
            throw new NullPointerException("product should not be NULL!");
        }
        if (this.bizContent.type == null) {
            throw new NullPointerException("type should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.equipmentId)) {
            throw new NullPointerException("equipment_id should not be NULL!");
        }
        if (this.bizContent.equipmentStatus == null) {
            throw new NullPointerException("equipment_status should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.time)) {
            throw new NullPointerException("time should not be NULL!");
        }
        return true;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayHeartbeatSynRequestBuilder{");
        sb.append("bizContent=").append(this.bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }


    public AlipayHeartbeatSynRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayHeartbeatSynRequestBuilder) super.setAppAuthToken(appAuthToken);
    }


    public AlipayHeartbeatSynRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayHeartbeatSynRequestBuilder) super.setNotifyUrl(notifyUrl);
    }


    public String getMac() {
        return this.bizContent.mac;
    }


    public AlipayHeartbeatSynRequestBuilder setMac(String mac) {
        this.bizContent.mac = mac;
        return this;
    }


    public String getNetworkType() {
        return this.bizContent.networkType;
    }


    public AlipayHeartbeatSynRequestBuilder setNetworkType(String networkType) {
        this.bizContent.networkType = networkType;
        return this;
    }


    public String getBattery() {
        return this.bizContent.battery;
    }


    public AlipayHeartbeatSynRequestBuilder setBattery(String battery) {
        this.bizContent.battery = battery;
        return this;
    }


    public String getWifiMac() {
        return this.bizContent.wifiMac;
    }


    public AlipayHeartbeatSynRequestBuilder setWifiMac(String wifiMac) {
        this.bizContent.wifiMac = wifiMac;
        return this;
    }


    public String getWifiName() {
        return this.bizContent.wifiName;
    }


    public AlipayHeartbeatSynRequestBuilder setWifiName(String wifiName) {
        this.bizContent.wifiName = wifiName;
        return this;
    }


    public String getNetworkStatus() {
        return this.bizContent.networkStatus;
    }


    public AlipayHeartbeatSynRequestBuilder setNetworkStatus(String networkStatus) {
        this.bizContent.networkStatus = networkStatus;
        return this;
    }


    public String getBbsPosition() {
        return this.bizContent.bbsPosition;
    }


    public AlipayHeartbeatSynRequestBuilder setBbsPosition(String bbsPosition) {
        this.bizContent.bbsPosition = bbsPosition;
        return this;
    }


    public String getManufacturerPid() {
        return this.bizContent.manufacturerPid;
    }


    public AlipayHeartbeatSynRequestBuilder setManufacturerPid(String manufacturerPid) {
        this.bizContent.manufacturerPid = manufacturerPid;
        return this;
    }


    public String getProviderId() {
        return this.bizContent.providerId;
    }


    public AlipayHeartbeatSynRequestBuilder setProviderId(String providerId) {
        this.bizContent.providerId = providerId;
        return this;
    }


    public String getEquipmentId() {
        return this.bizContent.equipmentId;
    }


    public AlipayHeartbeatSynRequestBuilder setEquipmentId(String equipmentId) {
        this.bizContent.equipmentId = equipmentId;
        return this;
    }


    public String getEquipmentPosition() {
        return this.bizContent.equipmentPosition;
    }


    public AlipayHeartbeatSynRequestBuilder setEquipmentPosition(String equipmentPosition) {
        this.bizContent.equipmentPosition = equipmentPosition;
        return this;
    }


    public EquipStatus getEquipmentStatus() {
        return this.bizContent.equipmentStatus;
    }


    public AlipayHeartbeatSynRequestBuilder setEquipmentStatus(EquipStatus equipmentStatus) {
        this.bizContent.equipmentStatus = equipmentStatus;
        return this;
    }


    public List<ExceptionInfo> getExceptionInfoList() {
        return this.bizContent.exceptionInfoList;
    }


    public AlipayHeartbeatSynRequestBuilder setExceptionInfoList(List<ExceptionInfo> exceptionInfoList) {
        this.bizContent.exceptionInfoList = exceptionInfoList;
        return this;
    }


    public Map<String, Object> getExtendInfo() {
        return this.bizContent.extendInfo;
    }


    public AlipayHeartbeatSynRequestBuilder setExtendInfo(Map<String, Object> extendInfo) {
        this.bizContent.extendInfo = extendInfo;
        return this;
    }


    public String getIp() {
        return this.bizContent.ip;
    }


    public AlipayHeartbeatSynRequestBuilder setIp(String ip) {
        this.bizContent.ip = ip;
        return this;
    }


    public Product getProduct() {
        return this.bizContent.product;
    }


    public AlipayHeartbeatSynRequestBuilder setProduct(Product product) {
        this.bizContent.product = product;
        return this;
    }


    public String getStoreId() {
        return this.bizContent.storeId;
    }


    public AlipayHeartbeatSynRequestBuilder setStoreId(String storeId) {
        this.bizContent.storeId = storeId;
        return this;
    }


    public String getTime() {
        return this.bizContent.time;
    }


    public AlipayHeartbeatSynRequestBuilder setTime(String time) {
        this.bizContent.time = time;
        return this;
    }


    public List<TradeInfo> getTradeInfoList() {
        return this.bizContent.tradeInfoList;
    }


    public AlipayHeartbeatSynRequestBuilder setSysTradeInfoList(List<SysTradeInfo> sysTradeInfoList) {
        if (Utils.isListNotEmpty(sysTradeInfoList)) {
            this.bizContent.tradeInfoList = (List) new ArrayList(sysTradeInfoList);
        }
        return this;
    }

    public AlipayHeartbeatSynRequestBuilder setPosTradeInfoList(List<PosTradeInfo> posTradeInfoList) {
        if (Utils.isListNotEmpty(posTradeInfoList)) {
            this.bizContent.tradeInfoList = (List) new ArrayList(posTradeInfoList);
        }
        return this;
    }


    public Type getType() {
        return this.bizContent.type;
    }


    public AlipayHeartbeatSynRequestBuilder setType(Type type) {
        this.bizContent.type = type;
        return this;
    }


    public static class BizContent {
        private Product product;

        private Type type;

        @SerializedName("equipment_id")
        private String equipmentId;

        @SerializedName("equipment_status")
        private EquipStatus equipmentStatus;

        private String time;

        @SerializedName("manufacturer_app_id")
        private String manufacturerPid;

        @SerializedName("sys_service_provider_id")
        private String providerId;

        @SerializedName("store_id")
        private String storeId;

        @SerializedName("equipment_position")
        private String equipmentPosition;

        @SerializedName("bbs_position")
        private String bbsPosition;

        @SerializedName("network_status")
        private String networkStatus;

        @SerializedName("network_type")
        private String networkType;

        private String battery;

        @SerializedName("wifi_mac")
        private String wifiMac;

        @SerializedName("wifi_name")
        private String wifiName;

        private String ip;

        private String mac;

        @SerializedName("trade_info")
        private List<TradeInfo> tradeInfoList;

        @SerializedName("exception_info")
        private List<ExceptionInfo> exceptionInfoList;
        @SerializedName("extend_info")
        private Map<String, Object> extendInfo;

        public String toString() {
            StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("product=").append(this.product);
            sb.append(", type=").append(this.type);
            sb.append(", equipmentId='").append(this.equipmentId).append('\'');
            sb.append(", equipmentStatus=").append(this.equipmentStatus);
            sb.append(", time='").append(this.time).append('\'');
            sb.append(", manufacturerPid='").append(this.manufacturerPid).append('\'');
            sb.append(", providerId='").append(this.providerId).append('\'');
            sb.append(", storeId='").append(this.storeId).append('\'');
            sb.append(", equipmentPosition='").append(this.equipmentPosition).append('\'');
            sb.append(", bbsPosition='").append(this.bbsPosition).append('\'');
            sb.append(", networkStatus='").append(this.networkStatus).append('\'');
            sb.append(", networkType='").append(this.networkType).append('\'');
            sb.append(", battery='").append(this.battery).append('\'');
            sb.append(", wifiMac='").append(this.wifiMac).append('\'');
            sb.append(", wifiName='").append(this.wifiName).append('\'');
            sb.append(", ip='").append(this.ip).append('\'');
            sb.append(", mac='").append(this.mac).append('\'');
            sb.append(", tradeInfoList=").append(this.tradeInfoList);
            sb.append(", exceptionInfoList=").append(this.exceptionInfoList);
            sb.append(", extendInfo=").append(this.extendInfo);
            sb.append('}');
            return sb.toString();
        }
    }
}



