package com.bitcom.pay.alipay.serviceImpl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoRefundFlow;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoRefundFlowMapper;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.sdk.alipay.util.AlipayUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("alipayRefundServiceImpl")
public class AliPayRefundServiceImpl
        implements IRefundService {
    private Logger logger = LoggerFactory.getLogger(AliPayRefundServiceImpl.class);

    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Autowired
    private InfoRefundFlowMapper infoRefundFlowMapper;


    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        this.logger.info("【支付宝退费】outTradeNo={},outRefundNo={},refundFee={}", new Object[]{outTradeNo, outRefundNo, refundFee});

        try {
            InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);
            AlipayClient alipayClient = AlipayUtil.getAlipayClient();
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(outTradeNo);
            model.setTradeNo(flow.getTradeNo());
            model.setRefundAmount(refundFee);
            model.setOutRequestNo(outRefundNo);
            request.setBizModel((AlipayObject) model);
            AlipayTradeRefundResponse response = (AlipayTradeRefundResponse) alipayClient.execute((AlipayRequest) request);
            if (!response.isSuccess()) {
                this.logger.error("【支付宝退费】失败");
                return ReturnObject.error(response.getMsg());
            }


            InfoRefundFlow refund = new InfoRefundFlow();
            String refundFlowNid = UUIDUtils.getUUID();
            refund.setNid(refundFlowNid);
            refund.setTradeNo(flow.getTradeNo());
            refund.setOutRefundNo(outRefundNo);
            refund.setRefundFee(refundFee);
            refund.setRefundId("-");
            refund.setRefundTime(new Date());
            this.infoRefundFlowMapper.insert(refund);

            this.logger.info("【支付宝退费】成功");
            return ReturnObject.success(null, refundFlowNid);
        } catch (Exception e) {
            this.logger.error("【支付宝退费】失败,cause={}", e.getMessage());
            throw e;
        }
    }
}



