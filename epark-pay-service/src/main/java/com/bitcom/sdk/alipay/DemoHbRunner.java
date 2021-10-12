package com.bitcom.sdk.alipay;

import com.bitcom.sdk.alipay.model.builder.AlipayHeartbeatSynRequestBuilder;
import com.bitcom.sdk.alipay.model.hb.EquipStatus;
import com.bitcom.sdk.alipay.model.hb.ExceptionInfo;
import com.bitcom.sdk.alipay.model.hb.Product;
import com.bitcom.sdk.alipay.model.hb.SysTradeInfo;
import com.bitcom.sdk.alipay.model.hb.Type;
import com.bitcom.sdk.alipay.service.AlipayMonitorService;
import com.bitcom.sdk.alipay.service.impl.hb.AbsHbRunner;
import com.bitcom.sdk.alipay.service.impl.hb.HbQueue;
import com.bitcom.sdk.alipay.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DemoHbRunner
        extends AbsHbRunner {
    public DemoHbRunner(AlipayMonitorService monitorService) {
        super(monitorService);
    }


    public String getAppAuthToken() {
        return null;
    }


    public AlipayHeartbeatSynRequestBuilder getBuilder() {
        List<SysTradeInfo> sysTradeInfoList = HbQueue.poll();


        List<ExceptionInfo> exceptionInfoList = new ArrayList<ExceptionInfo>();


        AlipayHeartbeatSynRequestBuilder builder = (new AlipayHeartbeatSynRequestBuilder()).setProduct(Product.FP).setType(Type.CR).setEquipmentId("cr1000001").setEquipmentStatus(EquipStatus.NORMAL).setTime(Utils.toDate(new Date())).setStoreId("store10001").setMac("0a:00:27:00:00:00").setNetworkType("LAN").setProviderId("2088911212323549").setSysTradeInfoList(sysTradeInfoList).setExceptionInfoList(exceptionInfoList);

        return builder;
    }
}



