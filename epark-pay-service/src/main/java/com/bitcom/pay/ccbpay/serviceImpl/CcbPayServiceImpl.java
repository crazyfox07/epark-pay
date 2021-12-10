package com.bitcom.pay.ccbpay.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.AbstractCommonPayService;
import com.bitcom.base.domain.InfoCcbPay;
import com.bitcom.base.mapper.InfoCcbPayMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.CryptoUtil;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.CcbConfig;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayInfo;
import com.bitcom.sdk.wechat.util.TenpayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;

@Service("ccbPayServiceImpl")
public class CcbPayServiceImpl extends AbstractCommonPayService {
    private Logger logger = LoggerFactory.getLogger(CcbPayServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    public static final String PAY_SCHEME = CcbConfig.PAY_SCHEME;

    public static final String PAY_TYPE = CcbConfig.PAY_TYPE;

    private final String CcbHost = CcbConfig.CcbHost;

    // 商户信息
    private final String MERCHANTID = CcbConfig.MERCHANTID;
    private final String POSID = CcbConfig.POSID;
    private final String BRANCHID = CcbConfig.BRANCHID;
    // 获取柜台完整公钥
    private final String pubKey = "30819d300d06092a864886f70d010101050003818b00308187028181009b5a97affd892036eddb0ad57122967e5850e1e6ff65f91dbf09e45a3f477595b0333bc0f0abe0fe616048785c46ba5c9d3f59c6fd63f4f3ffd3bb61fa4b6354c88df178cc61440963663c9d29edc88d7850d560ff89c3e5bd7208546d6adca2ea70be44c6948955242aa2eb554edf669bccc8d7a21c58b44f2fb56fb04f8763020111";

    @Autowired
    private InfoCcbPayMapper infoCcbPayMapper;

    @Value("${epark.callbackUrl}")
    private String callbackUrl;

    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject officialAccountPay(JSONObject bizProtocol) throws Exception {
        try {

            String bizName = bizProtocol.getString("bizName");
            JSONObject paramsJson = bizProtocol.getJSONObject("params");
            JSONObject attach = bizProtocol.getJSONObject("attach");
            Double totalAmount = Double.valueOf(Double.parseDouble(paramsJson.getString("totalAmount")));
            String totalFee = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            // todo 待删
//            totalFee = "0.01";

            String outTradeNo = TenpayUtil.createOutTradeNo();
            this.logger.info("【建行龙支付】下单outTradeNo={}", outTradeNo);

            String merInfo = "MERCHANTID=" + MERCHANTID + "&POSID=" + POSID + "&BRANCHID=" + BRANCHID;
            String pub = pubKey.substring(pubKey.length() - 30);
            //CURCODE:币种 缺省为01 －人民币， TXCODE: 交易码， TYPE:接口类型 1- 防钓鱼接口
            String paramMac1 = merInfo + "&ORDERID=" + outTradeNo + "&PAYMENT=" + totalFee +
                    "&CURCODE=01&TXCODE=530550&REMARK1=&REMARK2=&RETURNTYPE=3&TIMEOUT=";
            // pub:公钥后30位
            String paramMac2 =  "&PUB=" + pub;
            // GATEWAY:网关类型 CLIENTIP:客户端IP，REGINFO: 客户注册信息，PROINFO：商品信息，REFERER：商户URL
//            String position = paramsJson.getString("position");
//            String parkCode = paramsJson.getString("parkCode");
//            String devCode = paramsJson.getString("devCode");
//            String THIRDAPPINFO = position + "," + parkCode + "," + devCode;
//            String paramMac3 = "&GATEWAY=0&CLIENTIP=&REGINFO=&PROINFO=&REFERER=";
            String paramMac = paramMac1 + paramMac2;
            String mac = CryptoUtil.strToMD5(paramMac);
            String param = "?CCB_IBSVersion=V6&" + paramMac1;

            String url = CcbHost + param + "&MAC=" + mac;

            System.out.println(url);
//            String response = restTemplate.getForObject(url, String.class);
            String response = restTemplate.postForObject(url,null, String.class);
            this.logger.info("【建行龙支付】返回：{}", response);
            JSONObject resJson = JSONObject.parseObject(response);
            String result = "调用支付页面失败";
            if(resJson.getString("SUCCESS").equals("true")){
                String payUrl = resJson.getString("PAYURL");
                String response2 = restTemplate.getForObject(payUrl, String.class);
                JSONObject resJson2 = JSONObject.parseObject(response2);
                if(resJson2.getString("SUCCESS").equals("true")){
                    String qrUrl = resJson2.getString("QRURL");
                    result = URLDecoder.decode(qrUrl, "UTF-8");
//                    System.out.println(result);
                    // todo 待删
//                    result = restTemplate.getForObject(result, String.class);
                }
            }
            CachedAttach cacheAttach = new CachedAttach();
            PayInfo payInfo = new PayInfo();
            payInfo.setPayScheme(PAY_SCHEME);
            payInfo.setPayType(PAY_TYPE);

            payInfo.setBizName(bizName);
            cacheAttach.setPayInfo(payInfo);
            cacheAttach.setAttach(attach);
            int res = savePrePayInfo(outTradeNo, cacheAttach);
            this.logger.info("【建行支付支付】cache table result={}", Integer.valueOf(res));
            return ReturnObject.success(null, result);
        } catch (Exception e) {
            this.logger.error( "【建行龙支付】下单失败.{}", e.getMessage(), e);
            this.logger.error("bizProtocol: " + JSONObject.toJSONString(bizProtocol));
            throw e;
        }
    }

    @Override
    public ReturnObject appPay(JSONObject paramJSONObject) throws Exception {
        return null;
    }

    @Override
    public ReturnObject businessScan(JSONObject paramJSONObject) throws Exception {
        return null;
    }

    @Override
    public ReturnObject scanPay() throws Exception {
        return null;
    }

    private int savePrePayInfo(String outTradeNo, CachedAttach cacheAttach) {
        InfoCcbPay infoCcbPay = new InfoCcbPay();
        infoCcbPay.setOutTradeNo(outTradeNo);
        infoCcbPay.setAttach(JSON.toJSONString(cacheAttach));
//        return this.infoCcbPayMapper.insertSelective(infoCcbPay);
        return this.infoCcbPayMapper.insert(infoCcbPay);
    }
}
