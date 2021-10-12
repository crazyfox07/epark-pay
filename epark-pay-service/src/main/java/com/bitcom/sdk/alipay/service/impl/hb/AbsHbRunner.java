package com.bitcom.sdk.alipay.service.impl.hb;

import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.bitcom.config.AlipayConfig;
import com.bitcom.sdk.alipay.model.builder.AlipayHeartbeatSynRequestBuilder;
import com.bitcom.sdk.alipay.service.AlipayMonitorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class AbsHbRunner
        implements Runnable {
    protected Log log = LogFactory.getLog(getClass());


    private ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();

    private AlipayMonitorService monitorService;
    private long delay = 0L;
    private long duration = 0L;


    public AbsHbRunner(AlipayMonitorService monitorService) {
        this.monitorService = monitorService;
    }

    public abstract AlipayHeartbeatSynRequestBuilder getBuilder();

    public abstract String getAppAuthToken();

    public void run() {
        AlipayHeartbeatSynRequestBuilder builder = getBuilder();
        builder.setAppAuthToken(getAppAuthToken());

        MonitorHeartbeatSynResponse response = this.monitorService.heartbeatSyn(builder);


        StringBuilder sb = (new StringBuilder(response.getCode())).append(":").append(response.getMsg());
        if (StringUtils.isNotEmpty(response.getSubCode())) {
            sb.append(", ")
                    .append(response.getSubCode())
                    .append(":")
                    .append(response.getSubMsg());
        }
        this.log.info(sb.toString());
    }

    public void schedule() {
        if (this.delay == 0L) {
            this.delay = AlipayConfig.heartbeatDelay;
        }
        if (this.duration == 0L) {
            this.duration = AlipayConfig.cancelDuration;
        }
        this.scheduledService.scheduleAtFixedRate(this, this.delay, this.duration, TimeUnit.SECONDS);
    }


    public void shutdown() {
        this.scheduledService.shutdown();
    }


    public long getDelay() {
        return this.delay;
    }


    public void setDelay(long delay) {
        this.delay = delay;
    }


    public long getDuration() {
        return this.duration;
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }
}



