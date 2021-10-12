package com.bitcom.sdk.wechat.protocol.downloadbill_protocol;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.RandomStringGenerator;
import com.bitcom.sdk.wechat.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class DownloadBillReqData {
    private String appid = "";
    private String mch_id = "";
    private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String bill_date = "";
    private String bill_type = "";


    public DownloadBillReqData(String deviceInfo, String billDate, String billType) {
        setAppid(Configure.getAppid());


        setMch_id(Configure.getMchid());


        setDevice_info(deviceInfo);

        setBill_date(billDate);

        setBill_type(billType);


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


    public String getBill_date() {
        return this.bill_date;
    }


    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }


    public String getBill_type() {
        return this.bill_type;
    }


    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
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



