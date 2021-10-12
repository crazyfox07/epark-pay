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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("chinaUmsWechatPayServiceImpl")
public class ChinaUmsWechatPayServiceImpl
        extends AbstractCommonPayService {
    private static Logger logger = LoggerFactory.getLogger(ChinaUmsWechatPayServiceImpl.class);


    public static final String PAY_SCHEME = "0605";


    public static final String PAY_TYPE = "0804";


    @Value("${epark.callbackUrl}")
    private String callbackUrl;


    @Autowired
    private InfoUmsPayMapper infoUmsPayMapper;


    @Autowired
    @Qualifier("chinaUmsAliPayServiceImpl")
    private AbstractCommonPayService aliPayServiceImpl;


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
        request.setUrl(ChinaUmsConfig.wxpayUrl);
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


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        return null;
    }


    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        return this.aliPayServiceImpl.officialAccountPay(bizProtocol);
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



