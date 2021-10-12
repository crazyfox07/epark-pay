package com.bitcom.sdk.wechat.protocol.pay_protocol;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.RandomStringGenerator;
import com.bitcom.sdk.wechat.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ScanPayReqData {
    private String appid = "";
    private String mch_id = "";
    private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String body = "";
    private String attach = "";
    private String out_trade_no = "";
    private int total_fee = 0;
    private String spbill_create_ip = "";
    private String time_start = "";
    private String time_expire = "";
    private String goods_tag = "";
    private String auth_code = "";


    public ScanPayReqData(String authCode, String body, String attach, String outTradeNo, int totalFee, String deviceInfo, String spBillCreateIP, String timeStart, String timeExpire, String goodsTag) {
        setAppid(Configure.getAppid());


        setMch_id(Configure.getMchid());


        setAuth_code(authCode);


        setBody(body);


        setAttach(attach);


        setOut_trade_no(outTradeNo);


        setTotal_fee(totalFee);


        setDevice_info(deviceInfo);


        setSpbill_create_ip(spBillCreateIP);


        setTime_start(timeStart);


        setTime_expire(timeExpire);


        setGoods_tag(goodsTag);


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


    public String getDevice_info() {
        return this.device_info;
    }


    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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


    public String getBody() {
        return this.body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public String getAttach() {
        return this.attach;
    }


    public void setAttach(String attach) {
        this.attach = attach;
    }


    public String getOut_trade_no() {
        return this.out_trade_no;
    }


    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public int getTotal_fee() {
        return this.total_fee;
    }


    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }


    public String getSpbill_create_ip() {
        return this.spbill_create_ip;
    }


    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }


    public String getTime_start() {
        return this.time_start;
    }


    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }


    public String getTime_expire() {
        return this.time_expire;
    }


    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }


    public String getGoods_tag() {
        return this.goods_tag;
    }


    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }


    public String getAuth_code() {
        return this.auth_code;
    }


    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
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



