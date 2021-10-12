package com.bitcom.pay.wechat.serviceImpl;

import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoRefundFlow;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoRefundFlowMapper;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.config.WechatConfig;
import com.bitcom.sdk.wechat.PrepayIdRequestHandler;
import com.bitcom.sdk.wechat.ResponseHandler;
import com.bitcom.sdk.wechat.util.HttpsRequest;
import com.bitcom.sdk.wechat.util.WXUtil;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("wechatPayRefundServiceImpl")
public class WechatPayRefundServiceImpl
        implements IRefundService {
    private Logger logger = LoggerFactory.getLogger(WechatPayRefundServiceImpl.class);


    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Autowired
    private InfoRefundFlowMapper infoRefundFlowMapper;


    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        this.logger.info("【微信退费】outTradeNo={},outRefundNo={},refundFee={}", new Object[]{outTradeNo, outRefundNo, refundFee});
        try {
            String cerPwd, cerPath, appId, mchKey, mchId;
            InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);


            String apiChannel = flow.getPayApiChannel();
            if (StringUtils.equals(apiChannel, "1001")) {
                mchId = WechatConfig.MchID;
                mchKey = WechatConfig.Mch_KEY;
                appId = WechatConfig.AppID;
                cerPath = WechatConfig.CERT_PATH;
                cerPwd = WechatConfig.MchID;
            } else {
                mchId = WechatConfig.mch_id;
                mchKey = WechatConfig.mch_key;
                appId = WechatConfig.appid;
                cerPath = WechatConfig.cert_path;
                cerPwd = WechatConfig.mch_id;
            }

            PrepayIdRequestHandler requestHandler = new PrepayIdRequestHandler();
            requestHandler.setKey(mchKey);
            requestHandler.setParameter("appid", appId);
            requestHandler.setParameter("mch_id", mchId);
            requestHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            requestHandler.setParameter("transaction_id", flow.getTradeNo());
            requestHandler.setParameter("out_refund_no", outRefundNo);
            String totalAmount = flow.getTotalAmount();
            Double total = Double.valueOf(Double.valueOf(totalAmount).doubleValue() * 100.0D);
            Double refundFeeDouble = Double.valueOf(Double.valueOf(refundFee).doubleValue() * 100.0D);
            requestHandler.setParameter("total_fee", String.valueOf(total.intValue()));
            requestHandler.setParameter("refund_fee", String.valueOf(refundFeeDouble.intValue()));
            requestHandler.createSign();
            HttpsRequest request = new HttpsRequest(cerPath, cerPwd);
            String resContent = request.sendPost("https://api.mch.weixin.qq.com/secapi/pay/refund", requestHandler.getAllParameters());
            this.logger.info("【微信退费】请求返回: {}", resContent);

            ResponseHandler responseHandler = new ResponseHandler(null, null);
            responseHandler.doParse(resContent);
            String returnCode = responseHandler.getParameter("return_code");
            if (!StringUtils.equals("SUCCESS", returnCode)) {
                this.logger.error("【微信退费】失败,cause={}", responseHandler.getParameter("return_msg"));
                return ReturnObject.error(responseHandler.getParameter("return_msg"));
            }

            String resultCode = responseHandler.getParameter("result_code");
            if (!StringUtils.equals("SUCCESS", resultCode)) {
                this.logger.error("【微信退费】失败,cause={}", responseHandler.getParameter("err_code_des"));
                return ReturnObject.error(responseHandler.getParameter("err_code_des"));
            }

            responseHandler.setKey(mchKey);
            if (!responseHandler.isValidSign()) {
                this.logger.error("【微信退费】验签失败");
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
            this.logger.info("【微信退费】成功");
            return ReturnObject.success(null, refundFlowNid);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw e;
        }
    }
}



