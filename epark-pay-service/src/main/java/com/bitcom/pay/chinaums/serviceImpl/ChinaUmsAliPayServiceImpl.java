package com.bitcom.pay.chinaums.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.base.domain.InfoUmsPay;
import com.bitcom.base.mapper.InfoUmsPayMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.ChinaUmsConfig;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayInfo;
import com.bitcom.sdk.chinaums.ChinaUmsRequest;
import com.bitcom.sdk.chinaums.util.ChinaUmsUtil;
import com.bitcom.sdk.wechat.util.WXUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("chinaUmsAliPayServiceImpl")
public class ChinaUmsAliPayServiceImpl
        extends AbstractCommonPayService {
    private static Logger logger = LoggerFactory.getLogger(ChinaUmsAliPayServiceImpl.class);


    public static final String PAY_SCHEME = "0605";


    public static final String PAY_TYPE = "0805";


    @Value("${epark.callbackUrl}")
    private String callbackUrl;


    @Autowired
    private InfoUmsPayMapper infoUmsPayMapper;


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
        String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
        String mid = paramsJson.getString("mid");
        String tid = paramsJson.getString("tid");
        if (StringUtils.isEmpty(mid) || StringUtils.isEmpty(tid)) {
            return ReturnObject.error("银商对接，mid和tid为必选项。");
        }
        String merOrderId = ChinaUmsUtil.getMerOrderId();
        logger.info("【银联商务】App支付。 merOrderId={}", merOrderId);

        ChinaUmsRequest request = new ChinaUmsRequest();
        request.setUrl(ChinaUmsConfig.alipayUrl);
        request.setAppId(ChinaUmsConfig.appId);
        request.setAppKey(ChinaUmsConfig.appKey);
        request.setParameter("requestTimestamp", ChinaUmsUtil.getTimestamp());
        request.setParameter("merOrderId", merOrderId);
        request.setParameter("instMid", "APPDEFAULT");
        request.setParameter("tradeType", "APP");
        request.setParameter("mid", mid);
        request.setParameter("tid", tid);
        request.setParameter("orderDesc", paramsJson.getString("body"));
        request.setParameter("totalAmount", totalFee);
        request.setParameter("notifyUrl", this.callbackUrl + "/chinaUms/payNotify");
        String response = request.sendRequest();

        logger.info("【银联商务】App支付。下单返回={}", response);

        JSONObject responseJson = (JSONObject) JSONObject.parse(response);
        String errCode = responseJson.getString("errCode");
        if (!StringUtils.equals(errCode, "SUCCESS")) {
            logger.error("【银联商务】App支付下单失败，msg={}", responseJson.getString("errMsg"));
            return ReturnObject.error(responseJson.getString("errMsg"));
        }


        CachedAttach cacheAttach = new CachedAttach();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayScheme(PAY_SCHEME);
        payInfo.setPayType(PAY_TYPE);
        payInfo.setBizName(bizName);
        cacheAttach.setPayInfo(payInfo);
        cacheAttach.setAttach(attach);
        int res = savePrePayInfo(merOrderId, cacheAttach);
        logger.info("【银联商务】App pay.cache table result={}", Integer.valueOf(res));

        return ReturnObject.success(null, JSONObject.toJSON(responseJson.get("appPayRequest")));
    }


    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")) * 100.0D);
        String totalFee = Integer.toString((int) NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
        String mid = paramsJson.getString("mid");
        String tid = paramsJson.getString("tid");
        if (StringUtils.isEmpty(mid) || StringUtils.isEmpty(tid)) {
            return ReturnObject.error("银商对接，mid和tid为必选项。");
        }

        String merOrderId = ChinaUmsUtil.getMerOrderId();
        logger.info("【银联商务】jsApi。merOrderId={}", merOrderId);

        ChinaUmsRequest request = new ChinaUmsRequest();
        request.setUrl(ChinaUmsConfig.jsApiUrl);
        request.setAppId(ChinaUmsConfig.appId);
        request.setAppKey(ChinaUmsConfig.appKey);
        request.setParameter("requestTimestamp", ChinaUmsUtil.getTimestamp());
        request.setParameter("merOrderId", merOrderId);
        request.setParameter("mid", mid);
        request.setParameter("tid", tid);
        request.setParameter("instMid", "QRPAYDEFAULT");
        request.setParameter("body", paramsJson.getString("body"));
        request.setParameter("totalAmount", totalFee);
        request.setParameter("notifyUrl", this.callbackUrl + "/chinaUms/payNotify");

        String content = JSONObject.toJSONString(request.getAllParameters());
        String timestamp = ChinaUmsUtil.getTimestamp();
        String nonce = WXUtil.getNonceStr();
        String sign = request.getSignature(timestamp, nonce, content);

        Map<String, String> map = new HashMap<String, String>();
        map.put("authorization", "OPEN-FORM-PARAM");
        map.put("appId", ChinaUmsConfig.appId);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);
        map.put("content", content);
        map.put("signature", sign);

        String chinaUmsUrl = ChinaUmsConfig.jsApiUrl;
        chinaUmsUrl = chinaUmsUrl + "?appId=" + ChinaUmsConfig.appId;
        chinaUmsUrl = chinaUmsUrl + "&authorization=OPEN-FORM-PARAM";
        chinaUmsUrl = chinaUmsUrl + "&signature=" + URLEncoder.encode(sign, "UTF-8");
        chinaUmsUrl = chinaUmsUrl + "&nonce=" + nonce;
        chinaUmsUrl = chinaUmsUrl + "&content=" + URLEncoder.encode(content, "UTF-8");
        chinaUmsUrl = chinaUmsUrl + "&timestamp=" + timestamp;
        map.put("url", chinaUmsUrl);


        CachedAttach cacheAttach = new CachedAttach();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayScheme(PAY_SCHEME);
        payInfo.setPayType(PAY_TYPE);
        payInfo.setBizName(bizName);
        cacheAttach.setPayInfo(payInfo);
        cacheAttach.setAttach(attach);
        int res = savePrePayInfo(merOrderId, cacheAttach);
        logger.info("【银联商务】jsAPi.cache table result={}", Integer.valueOf(res));

        return ReturnObject.success(null, map);
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        return null;
    }


    public ReturnObject scanPay() throws Exception {
        return null;
    }


    private int savePrePayInfo(String merOrderId, CachedAttach cacheAttach) {
        InfoUmsPay pay = new InfoUmsPay();
        pay.setMerOrderId(merOrderId);
        pay.setAttach(JSONObject.toJSONString(cacheAttach));
        int res = this.infoUmsPayMapper.insertSelective(pay);
        return res;
    }
}



