package com.bitcom.pay.alipay.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoZfbPay;
import com.bitcom.base.mapper.InfoZfbPayMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.AlipayConfig;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayInfo;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.alipay.AliPay;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;
import com.bitcom.sdk.alipay.util.AlipayUtil;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("aliPayServiceImpl")
public class AliPayServiceImpl
        extends AbstractCommonPayService {
    private Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);


    @Value("${epark.callbackUrl}")
    private String callbackUrl;
    @Value("${epark.callbackUrlZfb}")
    private String callbackUrlZfb;

    @Autowired
    private InfoZfbPayMapper infoZfbPayMapper;


    @Autowired
    private ITradeFlowService tradeFlowService;


    public static final String PAY_SCHEME = "0603";


    public static final String PAY_TYPE = "0805";


    public ReturnObject appPay(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")));
        String totalFee = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
        String outTradeNo = AlipayUtil.getOutOrderNo();
        this.logger.info("【支付宝APP】支付outTradeNo={}", outTradeNo);


        Map<String, String> map = new TreeMap<String, String>();
        map.put("app_id", AlipayConfig.APP_ID);
        map.put("method", "alipay.trade.app.pay");
        map.put("format", "JSON");
        map.put("charset", "utf-8");
        map.put("sign_type", "RSA2");
        map.put("timestamp", DateUtils.toTimeStr(new Date()));
        map.put("version", "1.0");
        map.put("notify_url", this.callbackUrlZfb + "/alipay/payNotify");


        JSONObject bizJson = new JSONObject();
        bizJson.put("body", paramsJson.getString("body"));
        bizJson.put("subject", paramsJson.getString("subject"));
        bizJson.put("out_trade_no", outTradeNo);
        bizJson.put("timeout_express", "1m");
        bizJson.put("total_amount", totalFee);
        bizJson.put("product_code", "QUICK_MSECURITY_PAY");
        bizJson.put("goods_type", "0");
        map.put("biz_content", bizJson.toJSONString());

        String content = AlipayUtil.createGetUrlString(map);
        this.logger.info("ali.app.sdk create url=" + content);

        String sign = AlipaySignature.rsaSign(content, AlipayConfig.PRIVATE_KEY_RSA2, "utf-8", "RSA2");
        map.put("sign", sign);

        map = AlipayUtil.encodeMapValue(map);
        this.logger.info("【支付宝App】 return to App={}", AlipayUtil.createGetUrlString(map));


        CachedAttach cacheAttach = new CachedAttach();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayScheme("0603");
        payInfo.setPayType("0805");
        payInfo.setBizName(bizName);
        cacheAttach.setPayInfo(payInfo);
        cacheAttach.setAttach(attach);

        int res = savePrePayInfo(outTradeNo, cacheAttach);
        this.logger.info("【支付宝App】cache table result={}", Integer.valueOf(res));

        return ReturnObject.success(null, AlipayUtil.createGetUrlString(map));
    }


    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {
            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");

            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")));
            String totalFee = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            String outTradeNo = AlipayUtil.getOutOrderNo();
            this.logger.info("【支付宝公众号】支付outTradeNo={}", outTradeNo);


            AlipayClient alipayClient = AlipayUtil.getAlipayClient();
            AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
            AlipayTradeCreateModel model = new AlipayTradeCreateModel();
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalFee);
            model.setSubject(paramsJson.getString("subject"));
            model.setBody(paramsJson.getString("body"));
            model.setBuyerId(paramsJson.getString("buyerId"));
            model.setTimeoutExpress("1m");
            request.setBizModel((AlipayObject) model);
            request.setNotifyUrl(this.callbackUrlZfb + "/alipay/payNotify");

            AlipayTradeCreateResponse response = (AlipayTradeCreateResponse) alipayClient.execute((AlipayRequest) request);

            if (response.isSuccess()) {
                CachedAttach cacheAttach = new CachedAttach();
                PayInfo payInfo = new PayInfo();
                payInfo.setPayScheme("0603");
                payInfo.setPayType("0805");
                payInfo.setBizName(bizName);
                cacheAttach.setPayInfo(payInfo);
                cacheAttach.setAttach(attach);

                int res = savePrePayInfo(outTradeNo, cacheAttach);
                this.logger.info("【支付宝公众号】 cache result={}", Integer.valueOf(res));

                return ReturnObject.success(null, response.getTradeNo());
            }
        } catch (Exception e) {
            this.logger.error("【支付宝公众号】Exception:{}", e.getMessage());
            throw e;
        }
        return ReturnObject.error("trade.sdk error");
    }


    public ReturnObject businessScan(JSONObject bizProtocol) throws Exception {
        String bizName = bizProtocol.getString("bizName");
        JSONObject paramsJson = bizProtocol.getJSONObject("params");
        JSONObject attach = bizProtocol.getJSONObject("attach");

        Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")));
        String totalFee = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
        String outTradeNo = AlipayUtil.getOutOrderNo();
        String body = paramsJson.getString("body");
        String subject = paramsJson.getString("subject");
        String authCode = paramsJson.getString("authCode");

        if (StringUtils.isEmpty(authCode)) {
            return ReturnObject.error("authCode missing Exception");
        }


        AliPay.initSDKConfiguration();
        AlipayF2FPayResult result = AliPay.doScanPayBusiness((new AlipayTradePayRequestBuilder())
                .setAuthCode(authCode).setOutTradeNo(outTradeNo).setSubject(subject)
                .setTotalAmount(totalFee).setBody(body).setTimeoutExpress("1m").setStoreId("bitcom"));

        this.logger.info("【Alipay->BIZScan】outTradeNo={}, res = {}", outTradeNo, result.getTradeStatus());
        if (result.isTradeSuccess() != true) {
            this.logger.error("【Alipay->BIZScan】sdk failure cause trade status=" + result.getTradeStatus());
            return ReturnObject.error("sdk failure cause trade status=" + result.getTradeStatus());
        }


        String buyer = result.getResponse().getBuyerUserId();
        String tradeNo = result.getResponse().getTradeNo();
        Date gmtPay = result.getResponse().getGmtPayment();
        int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, PAY_SCHEME, PAY_TYPE, "1003", totalFee, gmtPay, buyer);
        this.logger.info("【Alipay->BIZScan】save trade flow, res = {}", Integer.valueOf(c));


        PayMessage payMessage = new PayMessage();
        payMessage.setOutTradeNo(outTradeNo);
        payMessage.setBizName(bizName);
        payMessage.setPayScheme(PAY_SCHEME);
        payMessage.setPayType(PAY_TYPE);
        payMessage.setTotalAmount(totalFee);
        payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
        payMessage.setAttach(attach);

        this.logger.info("【Alipay->BIZScan】success,return payMessage={}", JSON.toJSONString(payMessage));
        return ReturnObject.success(null, JSON.toJSONString(payMessage));
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



