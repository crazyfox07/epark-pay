package com.bitcom.pay.swift.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.base.domain.InfoWxPay;
import com.bitcom.base.mapper.InfoWxPayMapper;
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
@Service("swiftWechatPayServiceImpl")
public class SwiftWechatPayServiceImpl
        extends AbstractCommonPayService {
    private Logger logger = LoggerFactory.getLogger(SwiftWechatPayServiceImpl.class);


    public static final String PAY_SCHEME = "0604";


    public static final String PAY_TYPE = "0804";


    @Value("${epark.callbackUrl}")
    private String callbackUrl;


    @Autowired
    private InfoWxPayMapper infoWxPayMapper;


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
            String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = TenpayUtil.createOutTradeNo();
            this.logger.info("【Swfit微信App】下单outTradeNo={}", outTradeNo);

            SwiftRequestHandler prepayReqHandler = new SwiftRequestHandler(SwiftConfig.private_key);
            prepayReqHandler.setParameter("service", "pay.weixin.raw.app");
            prepayReqHandler.setParameter("sign_type", "RSA_1_256");
            prepayReqHandler.setParameter("mch_id", SwiftConfig.mch_id);
            prepayReqHandler.setParameter("appid", SwiftConfig.AppID);
            prepayReqHandler.setParameter("out_trade_no", outTradeNo);
            prepayReqHandler.setParameter("body", paramsJson.getString("body"));
            prepayReqHandler.setParameter("total_fee", totalFee);
            prepayReqHandler.setParameter("mch_create_ip", paramsJson.getString("payIp"));
            prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/swift/wechat/payNotify");
            prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            prepayReqHandler.setParameter("sub_appid", SwiftConfig.AppID);
            prepayReqHandler.createSign();


            String resContent = prepayReqHandler.sendRequest();
            this.logger.info("【Swfit微信App】unifiedorder result={}", resContent);


            Map<String, String> map = XmlUtil.xmlToEntity(resContent);
            if ("0".equals(map.get("status"))) {
                CachedAttach cacheAttach = new CachedAttach();
                PayInfo payInfo = new PayInfo();
                payInfo.setPayScheme("0604");
                payInfo.setPayType("0804");
                payInfo.setBizName(bizName);
                cacheAttach.setPayInfo(payInfo);
                cacheAttach.setAttach(attach);
                int res = savePrePayInfo(outTradeNo, cacheAttach);
                this.logger.info("【Swfit微信App】cache table result={}", Integer.valueOf(res));


                String pay_info = map.get("pay_info");
                this.logger.info("【Swfit微信App】返回：{}", pay_info);
                return ReturnObject.success(null, JSONObject.parseObject(pay_info));
            }
            return ReturnObject.error((String) map.get("message") + (String) map.get("err_msg"));
        } catch (Exception e) {
            this.logger.error("【Swfit微信App】下单失败.{}", e.getMessage());
            throw e;
        }
    }


    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
            String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = TenpayUtil.createOutTradeNo();
            this.logger.info("【Swfit微信公众号】下单outTradeNo={}", outTradeNo);

            SwiftRequestHandler prepayReqHandler = new SwiftRequestHandler(SwiftConfig.private_key);
            prepayReqHandler.setParameter("service", "pay.weixin.jspay");
            prepayReqHandler.setParameter("sign_type", "RSA_1_256");
            prepayReqHandler.setParameter("mch_id", SwiftConfig.mch_id);
            prepayReqHandler.setParameter("is_raw", "1");
            prepayReqHandler.setParameter("out_trade_no", outTradeNo);
            prepayReqHandler.setParameter("body", paramsJson.getString("body"));
            prepayReqHandler.setParameter("sub_openid", paramsJson.getString("buyerId"));
            prepayReqHandler.setParameter("sub_appid", SwiftConfig.appid);
            prepayReqHandler.setParameter("total_fee", totalFee);
            prepayReqHandler.setParameter("mch_create_ip", paramsJson.getString("payIp"));
            prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/swift/wechat/payNotify");
            prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            prepayReqHandler.createSign();


            String resContent = prepayReqHandler.sendRequest();
            this.logger.info("【Swfit微信公众号】unifiedorder result={}", resContent);


            Map<String, String> map = XmlUtil.xmlToEntity(resContent);
            if ("0".equals(map.get("status")) && "0".equals(map.get("result_code"))) {
                CachedAttach cacheAttach = new CachedAttach();
                PayInfo payInfo = new PayInfo();
                payInfo.setPayScheme("0604");
                payInfo.setPayType("0804");
                payInfo.setBizName(bizName);
                cacheAttach.setPayInfo(payInfo);
                cacheAttach.setAttach(attach);
                int res = savePrePayInfo(outTradeNo, cacheAttach);
                this.logger.info("【Swfit微信公众号】cache table result={}", Integer.valueOf(res));

                String pay_info = map.get("pay_info");
                this.logger.info("【Swfit微信公众号】返回：{}", pay_info);
                JSONObject json = JSONObject.parseObject(pay_info);
                return ReturnObject.success(null, json);
            }
            return ReturnObject.error((String) map.get("message") + (String) map.get("err_msg"));
        } catch (Exception e) {
            this.logger.error("【Swfit微信公众号】下单失败.{}", e.getMessage());
            throw e;
        }
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    public ReturnObject scanPay() throws Exception {
        return ReturnObject.error("do not support yet.");
    }


    private int savePrePayInfo(String outTradeNo, CachedAttach cacheAttach) {
        InfoWxPay infoWxPay = new InfoWxPay();
        infoWxPay.setOutTradeNo(outTradeNo);
        infoWxPay.setAttach(JSON.toJSONString(cacheAttach));
        return this.infoWxPayMapper.insertSelective(infoWxPay);
    }
}



