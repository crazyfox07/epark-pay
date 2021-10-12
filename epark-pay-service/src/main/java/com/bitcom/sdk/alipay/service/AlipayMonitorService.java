package com.bitcom.sdk.alipay.service;

import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.bitcom.sdk.alipay.model.builder.AlipayHeartbeatSynRequestBuilder;

public interface AlipayMonitorService {
    MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynRequestBuilder paramAlipayHeartbeatSynRequestBuilder);
}



