package com.bitcom.pay.swift.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.base.domain.InfoSwiftcloudPay;
import com.bitcom.base.mapper.InfoSwiftcloudPayMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.SwiftConfig;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayInfo;
import com.bitcom.sdk.swfit.wechat.SwiftRequestHandler;
import com.bitcom.sdk.wechat.util.TenpayUtil;
import com.bitcom.sdk.wechat.util.WXUtil;
import com.bitcom.sdk.wechat.util.XmlUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("swiftCloudPayServiceImpl")
public class SwiftCloudPayServiceImpl
        extends AbstractCommonPayService {
    public static final Logger logger = LoggerFactory.getLogger(SwiftCloudPayServiceImpl.class);


    public static final String PAY_SCHEME = "0604";


    public static final String PAY_TYPE = "0806";


    @Value("${epark.callbackUrl}")
    private String callbackUrl;


    @Autowired
    private InfoSwiftcloudPayMapper infoSwiftcloudPayMapper;


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
            String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = TenpayUtil.createOutTradeNo();
            logger.info("【Swift银联JS】下单outTradeNo={}", outTradeNo);

            SwiftRequestHandler prepayReqHandler = new SwiftRequestHandler(SwiftConfig.private_key);
            prepayReqHandler.setParameter("service", "pay.unionpay.jspay");
            prepayReqHandler.setParameter("sign_type", "RSA_1_256");
            prepayReqHandler.setParameter("mch_id", SwiftConfig.mch_id);
            prepayReqHandler.setParameter("out_trade_no", outTradeNo);
            prepayReqHandler.setParameter("body", paramsJson.getString("body"));
            prepayReqHandler.setParameter("user_id", paramsJson.getString("buyerId"));
            prepayReqHandler.setParameter("customer_ip", SwiftConfig.terminalIp);
            prepayReqHandler.setParameter("total_fee", totalFee);
            prepayReqHandler.setParameter("mch_create_ip", paramsJson.getString("payIp"));
            prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/swift/cloudPay/payNotify");
            prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            prepayReqHandler.createSign();


            String resContent = prepayReqHandler.sendRequest();
            logger.info("【Swift银联JS】unifiedorder result={}", resContent);


            Map<String, String> map = XmlUtil.xmlToEntity(resContent);
            if ("0".equals(map.get("status")) && "0".equals(map.get("result_code"))) {
                CachedAttach cacheAttach = new CachedAttach();
                PayInfo payInfo = new PayInfo();
                payInfo.setPayScheme("0604");
                payInfo.setPayType("0806");
                payInfo.setBizName(bizName);
                cacheAttach.setPayInfo(payInfo);
                cacheAttach.setAttach(attach);
                int res = savePrePayInfo(outTradeNo, cacheAttach);
                logger.info("【Swift银联JS】cache table result={}", Integer.valueOf(res));

                String pay_url = map.get("pay_url");
                logger.info("【Swift银联JS】返回：{}", pay_url);
                return ReturnObject.success(null, pay_url);
            }
            return ReturnObject.error((String) map.get("message") + (String) map.get("err_msg"));
        } catch (Exception e) {
            logger.error("【Swift银联JS】下单失败.{}", e.getMessage());
            throw e;
        }
    }


    public ReturnObject scanPay() throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    private int savePrePayInfo(String outTradeNo, CachedAttach cacheAttach) {
        InfoSwiftcloudPay pay = new InfoSwiftcloudPay();
        pay.setOutTradeNo(outTradeNo);
        pay.setAttach(JSONObject.toJSONString(cacheAttach));
        return this.infoSwiftcloudPayMapper.insertSelective(pay);
    }
}



