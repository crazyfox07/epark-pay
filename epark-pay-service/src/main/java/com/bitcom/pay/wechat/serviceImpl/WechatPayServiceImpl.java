package com.bitcom.pay.wechat.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoWxPay;
import com.bitcom.base.mapper.InfoWxPayMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.WechatConfig;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayInfo;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.wechat.ClientRequestHandler;
import com.bitcom.sdk.wechat.PrepayIdRequestHandler;
import com.bitcom.sdk.wechat.WXPay;
import com.bitcom.sdk.wechat.business.ScanPayBusiness;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayReqData;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayResData;
import com.bitcom.sdk.wechat.util.TenpayUtil;
import com.bitcom.sdk.wechat.util.WXUtil;
import com.bitcom.sdk.wechat.util.XmlUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("wechatPayServiceImpl")
public class WechatPayServiceImpl
        extends AbstractCommonPayService {
    private Logger logger = LoggerFactory.getLogger(WechatPayServiceImpl.class);


    public static final String PAY_SCHEME = "0602";

    public static final String PAY_TYPE = "0804";

    @Value("${epark.callbackUrl}")
    private String callbackUrl;

    @Autowired
    private InfoWxPayMapper infoWxPayMapper;

    @Autowired
    private ITradeFlowService tradeFlowService;


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");
        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
        String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
        String outTradeNo = TenpayUtil.createOutTradeNo();
        this.logger.info("【微信APP支付】下单outTradeNo={}", outTradeNo);


        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();
        prepayReqHandler.setKey(WechatConfig.Mch_KEY);
        prepayReqHandler.setParameter("appid", WechatConfig.AppID);
        prepayReqHandler.setParameter("mch_id", WechatConfig.MchID);
        prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
        prepayReqHandler.setParameter("body", paramsJson.getString("body"));
        prepayReqHandler.setParameter("out_trade_no", outTradeNo);
        prepayReqHandler.setParameter("total_fee", totalFee);
        prepayReqHandler.setParameter("spbill_create_ip", paramsJson.getString("payIp"));
        prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/wechat/payNotify");
        prepayReqHandler.setParameter("trade_type", "APP");
        prepayReqHandler.createSign();

        String resContent = prepayReqHandler.sendPrepay();
        if (StringUtils.contains(resContent, "FAIL")) {
            this.logger.error("【微信App支付】ERROR.unifiedorder result={}", resContent);
            return ReturnObject.error(resContent);
        }


        CachedAttach cacheAttach = new CachedAttach();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayScheme(PAY_SCHEME);
        payInfo.setPayType(PAY_TYPE);
        payInfo.setBizName(bizName);
        cacheAttach.setPayInfo(payInfo);
        cacheAttach.setAttach(attach);
        int res = savePrePayInfo(outTradeNo, cacheAttach);


        Map<String, String> map = XmlUtil.xmlToEntity(resContent);
        ClientRequestHandler clientHandler = new ClientRequestHandler();
        clientHandler.setKey(WechatConfig.Mch_KEY);
        clientHandler.setParameter("appid", map.get("appid"));
        clientHandler.setParameter("partnerid", map.get("mch_id"));
        clientHandler.setParameter("prepayid", map.get("prepay_id"));
        clientHandler.setParameter("package", "Sign=WXPay");
        clientHandler.setParameter("noncestr", map.get("nonce_str"));
        clientHandler.setParameter("timestamp", WXUtil.getTimeStamp());
        clientHandler.createSign();

        this.logger.info("【微信App支付】返回APP：{}", JSON.toJSONString(clientHandler.getAllParameters()));
        return ReturnObject.success(null, clientHandler.getAllParameters());
    }


    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
            String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = TenpayUtil.createOutTradeNo();
            this.logger.info("【微信公众号支付】下单outTradeNo={}", outTradeNo);


            PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();
            prepayReqHandler.setKey(WechatConfig.mch_key);
            prepayReqHandler.setParameter("appid", WechatConfig.appid);
            prepayReqHandler.setParameter("mch_id", WechatConfig.mch_id);
            prepayReqHandler.setParameter("nonce_str", WXUtil.getNonceStr());
            prepayReqHandler.setParameter("body", paramsJson.getString("body"));
            prepayReqHandler.setParameter("out_trade_no", outTradeNo);
            prepayReqHandler.setParameter("total_fee", totalFee);
            prepayReqHandler.setParameter("spbill_create_ip", paramsJson.getString("payIp"));
            prepayReqHandler.setParameter("openid", paramsJson.getString("buyerId"));
            prepayReqHandler.setParameter("notify_url", this.callbackUrl + "/wechat/payNotify");
            prepayReqHandler.setParameter("trade_type", "JSAPI");
            prepayReqHandler.createSign();


            String resContent = prepayReqHandler.sendPrepay();
            this.logger.info("【微信公众号支付】unifiedorder result={}", resContent);
            if (StringUtils.contains(resContent, "FAIL")) {
                return ReturnObject.error(resContent);
            }


            CachedAttach cacheAttach = new CachedAttach();
            PayInfo payInfo = new PayInfo();
            payInfo.setPayScheme(PAY_SCHEME);
            payInfo.setPayType(PAY_TYPE);
            payInfo.setBizName(bizName);
            cacheAttach.setPayInfo(payInfo);
            cacheAttach.setAttach(attach);
            int res = savePrePayInfo(outTradeNo, cacheAttach);
            this.logger.info("【微信公众号支付】cache table result={}", Integer.valueOf(res));


            Map<String, String> map = XmlUtil.xmlToEntity(resContent);
            ClientRequestHandler clientHandler = new ClientRequestHandler();
            clientHandler.setKey(WechatConfig.mch_key);

            clientHandler.setParameter("appId", map.get("appid"));
            clientHandler.setParameter("timeStamp", WXUtil.getTimeStamp());
            clientHandler.setParameter("nonceStr", map.get("nonce_str"));
            clientHandler.setParameter("package", "prepay_id=" + (String) map.get("prepay_id"));
            clientHandler.setParameter("signType", "MD5");
            clientHandler.createSign();
            clientHandler.setParameter("paySign", clientHandler.getParameter("sign"));

            String tradeType = map.get("trade_type");
            if ("NATIVE".equals(tradeType)) {
                clientHandler.setParameter("codeUrl", map.get("code_url"));
            } else if (StringUtils.equals(tradeType, "MWEB")) {
                clientHandler.setParameter("mwebUrl", map.get("mweb_url"));
            }

            this.logger.info("【微信公众号支付】返回：{}", JSON.toJSONString(clientHandler.getAllParameters()));
            return ReturnObject.success(null, clientHandler.getAllParameters());
        } catch (Exception e) {
            this.logger.error("【微信公众号支付】下单失败.{}", e.getMessage());
            throw e;
        }
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
        int totalFee = (int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue());
        String outTradeNo = TenpayUtil.createOutTradeNo();
        String body = paramsJson.getString("body");
        String authCode = paramsJson.getString("authCode");
        String payIp = paramsJson.getString("payIp");

        if (StringUtils.isEmpty(authCode)) {
            return ReturnObject.error("authCode missing Exception");
        }


        String mchId = WechatConfig.mch_id;
        String mchKey = WechatConfig.mch_key;
        String appId = WechatConfig.appid;
        String certPath = WechatConfig.cert_path;
        String certPwd = WechatConfig.mch_id;
        WXPay.initSDKConfiguration(mchKey, appId, mchId, "", certPath, certPwd);

        ScanPayReqData scanPayReqData = new ScanPayReqData(authCode, body, "", outTradeNo, totalFee, "", payIp, "", "", "");

        final Map<String, String> map = new HashMap<String, String>();
        map.put("code", "1");
        map.put("message", "Unknown error");

        WXPay.doScanPayBusiness(scanPayReqData, new ScanPayBusiness.ResultListener() {
            public void onSuccess(ScanPayResData respData) {
                map.put("code", "0");
                map.put("openId", respData.getOpenid());
                map.put("tradeNo", respData.getTransaction_id());
                map.put("gmtPay", respData.getTime_end());
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】SUCCESS");
            }

            public void onFailBySignInvalid(ScanPayResData respData) {
                map.put("message", "SIGNERROR");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】SIGNERROR");
            }

            public void onFailByReturnCodeFail(ScanPayResData respData) {
                map.put("message", "FAIL");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】 FAIL");
            }

            public void onFailByReturnCodeError(ScanPayResData respData) {
                map.put("message", "PARAM_ERROR");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】PARAM_ERROR");
            }

            public void onFailByMoneyNotEnough(ScanPayResData respData) {
                map.put("message", "NOTENOUGH");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】 NOTENOUGH");
            }

            public void onFailByAuthCodeInvalid(ScanPayResData respData) {
                map.put("message", "AUTH_CODE_ERROR");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】 AUTH_CODE_ERROR");
            }

            public void onFailByAuthCodeExpire(ScanPayResData respData) {
                map.put("message", "AUTHCODEEXPIRE");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】 AUTHCODEEXPIRE");
            }

            public void onFail(ScanPayResData respData) {
                map.put("message", "FAIL");
                WechatPayServiceImpl.this.logger.info("【扫码支付-微信】 FAIL");
            }
        });

        if (StringUtils.equals("1", map.get("code"))) {
            return ReturnObject.error(map.get("message"));
        }


        String buyer = map.get("openId");
        String tradeNo = map.get("tradeNo");
        Date gmtPay = DateUtils.parseDate(map.get("gmtPay"), "yyyyMMddHHmmss");
        int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, PAY_SCHEME, PAY_TYPE, "1003", paramsJson.getString("totalAmount"), gmtPay, buyer);
        this.logger.info("【扫码支付-微信】save trade flow, res = {}", Integer.valueOf(c));


        PayMessage payMessage = new PayMessage();
        payMessage.setOutTradeNo(outTradeNo);
        payMessage.setBizName(bizName);
        payMessage.setPayScheme(PAY_SCHEME);
        payMessage.setPayType(PAY_TYPE);
        payMessage.setTotalAmount(paramsJson.getString("totalAmount"));
        payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
        payMessage.setAttach(attach);

        this.logger.info("【扫码支付-微信】success,return payMessage={}", JSON.toJSONString(payMessage));
        return ReturnObject.success(null, JSON.toJSONString(payMessage));
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



