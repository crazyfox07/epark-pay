package com.bitcom.pay.chinaums.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoRefundFlow;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoRefundFlowMapper;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.config.ChinaUmsConfig;
import com.bitcom.sdk.chinaums.ChinaUmsRequest;
import com.bitcom.sdk.chinaums.util.ChinaUmsUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("chinaUmsRefundServiceImpl")
public class ChinaUmsRefundServiceImpl
        implements IRefundService {
    private Logger logger = LoggerFactory.getLogger(ChinaUmsRefundServiceImpl.class);


    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;


    @Autowired
    private InfoRefundFlowMapper infoRefundFlowMapper;


    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        this.logger.info("【chinaUms退费】outTradeNo={},outRefundNo={},refundFee={}", new Object[]{outTradeNo, outRefundNo, refundFee});
        outRefundNo = ChinaUmsUtil.getMerOrderId();
        String refundAmount = String.valueOf(Integer.valueOf((Double.valueOf(refundFee).doubleValue() * 100.0D) + ""));

        InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);

        ChinaUmsRequest request = new ChinaUmsRequest();
        request.setUrl(ChinaUmsConfig.refundUrl);
        request.setAppId(ChinaUmsConfig.appId);
        request.setAppKey(ChinaUmsConfig.appKey);
        request.setParameter("requestTimestamp", ChinaUmsUtil.getTimestamp());
        request.setParameter("merOrderId", outTradeNo);
        request.setParameter("instMid", "APPDEFAULT");
        request.setParameter("mid", flow.getMerId());
        request.setParameter("tid", flow.getTermimalId());
        request.setParameter("refundAmount", refundAmount);
        request.setParameter("refundOrderId", outRefundNo);

        String response = request.sendRequest();
        this.logger.info("【chinaUms退费】请求返回: {}", response);

        JSONObject retJson = (JSONObject) JSONObject.parse(response);
        String errCode = retJson.getString("errCode");
        if (!"SUCCESS".equals(errCode)) {
            return ReturnObject.error(retJson.getString("errMsg"));
        }

        InfoRefundFlow refund = new InfoRefundFlow();
        String refundFlowNid = UUIDUtils.getUUID();
        refund.setNid(refundFlowNid);
        refund.setTradeNo(outTradeNo);
        refund.setOutRefundNo(outRefundNo);
        refund.setRefundFee(refundFee);
        this.infoRefundFlowMapper.insert(refund);
        refund.setRefundTime(new Date());
        return ReturnObject.success(null, refundFlowNid);
    }
}



