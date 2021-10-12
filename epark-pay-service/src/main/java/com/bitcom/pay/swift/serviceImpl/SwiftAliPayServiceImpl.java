package com.bitcom.pay.swift.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.base.domain.InfoZfbPay;
import com.bitcom.base.mapper.InfoZfbPayMapper;
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
@Service("swiftAliPayServiceImpl")
public class SwiftAliPayServiceImpl
        extends AbstractCommonPayService {
    public static final Logger logger = LoggerFactory.getLogger(SwiftAliPayServiceImpl.class);


    public static final String PAY_SCHEME = "0604";


    public static final String PAY_TYPE = "0805";


    @Value("${epark.callbackUrl}")
    private String callbackUrl;


    @Autowired
    private InfoZfbPayMapper infoZfbPayMapper;


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        return ReturnObject.error("swift do not support yet.");
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        return ReturnObject.error("we do not support yet.");
    }


    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
            String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = TenpayUtil.createOutTradeNo();
            logger.info("【swift alipay jsapi】下单outTradeNo={}", outTradeNo);

            SwiftRequestHandler prepayReqHandler = new SwiftRequestHandler(SwiftConfig.private_key);
            prepayReqHandler.setParameter("service", "pay.alipay.jspay");
            prepayReqHandler.setParameter("sign_type", "RSA_1_256");
            prepayReqHandler.setParameter("mch_id", SwiftConfig.mch_id);
            prepayReqHandler.setParameter("out_trade_no", outTradeNo);
            prepayReqHandler.setParameter("body", paramsJson.getString("body"));
            prepayReqHandler.setParameter("total_fee", totalFee);
            prepayReqHandler.setParameter("mch_create_ip", paramsJson.getString("payIp"));
            prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/swift/alipay/payNotify");
            prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            prepayReqHandler.setParameter("buyer_id", paramsJson.getString("buyerId"));

            prepayReqHandler.createSign();


            String resContent = prepayReqHandler.sendRequest();
            logger.info("【swift alipay jsapi】preOrder result={}", resContent);

            Map<String, String> map = XmlUtil.xmlToEntity(resContent);
            if ("0".equals(map.get("status")) && "0".equals(map.get("result_code"))) {
                CachedAttach cacheAttach = new CachedAttach();
                PayInfo payInfo = new PayInfo();
                payInfo.setPayScheme("0604");
                payInfo.setPayType("0805");
                payInfo.setBizName(bizName);
                cacheAttach.setPayInfo(payInfo);
                cacheAttach.setAttach(attach);
                int res = savePrePayInfo(outTradeNo, cacheAttach);
                logger.info("【swift alipay jsapi】cache table result={}", Integer.valueOf(res));
                String pay_info = map.get("pay_info");
                logger.info("【swift alipay jsapi】返回：{}", pay_info);
                return ReturnObject.success(null, JSONObject.parseObject(pay_info));
            }
            return ReturnObject.error((String) map.get("message") + (String) map.get("err_msg"));
        } catch (Exception e) {
            logger.error("【swift alipay jsapi】下单失败.{}", e.getMessage());
            throw e;
        }
    }


    public ReturnObject scanPay() throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    private int savePrePayInfo(String outTradeNo, CachedAttach bizAttach) {
        InfoZfbPay pay = new InfoZfbPay();
        pay.setOutTradeNo(outTradeNo);
        pay.setAttach(JSONObject.toJSONString(bizAttach));
        int res = this.infoZfbPayMapper.insertSelective(pay);

        return res;
    }
}



