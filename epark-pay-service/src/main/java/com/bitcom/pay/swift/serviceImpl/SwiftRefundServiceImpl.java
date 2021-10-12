package com.bitcom.pay.swift.serviceImpl;

import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoRefundFlow;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoRefundFlowMapper;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.config.SwiftConfig;
import com.bitcom.sdk.swfit.wechat.SwiftRequestHandler;
import com.bitcom.sdk.wechat.util.WXUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("swiftRefundServiceImpl")
public class SwiftRefundServiceImpl
        implements IRefundService {
    private Logger logger = LoggerFactory.getLogger(SwiftRefundServiceImpl.class);


    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Autowired
    private InfoRefundFlowMapper infoRefundFlowMapper;


    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        this.logger.info("【swift退费】outTradeNo={},outRefundNo={},refundFee={}", new Object[]{outTradeNo, outRefundNo, refundFee});

        try {
            InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);
            SwiftRequestHandler requestHandler = new SwiftRequestHandler(SwiftConfig.private_key);
            requestHandler.setParameter("service", "unified.trade.refund");
            requestHandler.setParameter("sign_type", "RSA_1_256");
            requestHandler.setParameter("mch_id", SwiftConfig.mch_id);
            requestHandler.setParameter("transaction_id", flow.getTradeNo());
            requestHandler.setParameter("out_refund_no", outRefundNo);

            String totalAmount = flow.getTotalAmount();
            Double total = Double.valueOf(Double.valueOf(totalAmount).doubleValue() * 100.0D);
            Double refundFeeDouble = Double.valueOf(Double.valueOf(refundFee).doubleValue() * 100.0D);
            requestHandler.setParameter("total_fee", String.valueOf(total.intValue()));
            requestHandler.setParameter("refund_fee", String.valueOf(refundFeeDouble.intValue()));
            requestHandler.setParameter("op_user_id", SwiftConfig.mch_id);
            requestHandler.setParameter("nonce_str", WXUtil.getNonceStr());

            requestHandler.createSign();

            String resContent = requestHandler.sendRequest();
            this.logger.info("【swift退费】请求返回: {}", resContent);

            SwiftRequestHandler responseHandler = new SwiftRequestHandler(SwiftConfig.swift_public_key);
            responseHandler.doParse(resContent);
            String status = responseHandler.getParameter("status");

            if (!"0".equals(status)) {
                this.logger.error("【swift退款】失败，msg={}", responseHandler.getParameter("err_code"));
                return ReturnObject.error(responseHandler.getParameter("err_code"));
            }

            String resultCode = responseHandler.getParameter("result_code");
            if (!"0".equals(resultCode)) {
                this.logger.error("【swift退款】失败，msg={}", responseHandler.getParameter("message"));
                return ReturnObject.error(responseHandler.getParameter("message"));
            }

            if (!responseHandler.validSign()) {
                this.logger.error("【swift退费】验签失败");
                return ReturnObject.error("退费验签失败");
            }


            InfoRefundFlow refund = new InfoRefundFlow();
            String refundFlowNid = UUIDUtils.getUUID();
            refund.setNid(refundFlowNid);
            refund.setTradeNo(responseHandler.getParameter("transaction_id"));
            refund.setOutRefundNo(outRefundNo);
            refund.setRefundFee(refundFee);
            refund.setRefundId(responseHandler.getParameter("refund_id"));
            refund.setRefundTime(new Date());
            this.infoRefundFlowMapper.insert(refund);

            this.logger.info("【swift退费】退款成功");
            return ReturnObject.success(null, refundFlowNid);
        } catch (Exception e) {
            this.logger.error("【swift退费】Exception,cause={}", e.getMessage());
            throw e;
        }
    }
}



