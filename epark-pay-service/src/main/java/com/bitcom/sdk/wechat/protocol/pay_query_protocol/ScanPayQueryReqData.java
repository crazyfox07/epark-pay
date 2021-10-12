package com.bitcom.sdk.wechat.protocol.pay_query_protocol;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.RandomStringGenerator;
import com.bitcom.sdk.wechat.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ScanPayQueryReqData {
    private String appid = "";
    private String mch_id = "";
    private String transaction_id = "";
    private String out_trade_no = "";
    private String nonce_str = "";
    private String sign = "";


    public ScanPayQueryReqData(String transactionID, String outTradeNo) {
        setAppid(Configure.getAppid());


        setMch_id(Configure.getMchid());


        setTransaction_id(transactionID);


        setOut_trade_no(outTradeNo);


        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));


        String sign = Signature.getSign(toMap());
        setSign(sign);
    }


    public String getAppid() {
        return this.appid;
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }


    public String getMch_id() {
        return this.mch_id;
    }


    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }


    public String getTransaction_id() {
        return this.transaction_id;
    }


    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }


    public String getOut_trade_no() {
        return this.out_trade_no;
    }


    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public String getNonce_str() {
        return this.nonce_str;
    }


    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }


    public String getSign() {
        return this.sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {

            try {
                Object obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}



