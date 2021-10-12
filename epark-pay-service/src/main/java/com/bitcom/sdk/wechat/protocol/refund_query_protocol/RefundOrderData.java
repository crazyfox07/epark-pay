package com.bitcom.sdk.wechat.protocol.refund_query_protocol;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public class RefundOrderData {
    private String outRefundNo = "";
    private String refundID = "";
    private String refundChannel = "";


    private int refundFee = 0;
    private int couponRefundFee = 0;
    private String refundStatus = "";


    public String getOutRefundNo() {
        return this.outRefundNo;
    }


    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }


    public String getRefundID() {
        return this.refundID;
    }


    public void setRefundID(String refundID) {
        this.refundID = refundID;
    }


    public String getRefundChannel() {
        return this.refundChannel;
    }


    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }


    public int getRefundFee() {
        return this.refundFee;
    }


    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }


    public int getCouponRefundFee() {
        return this.couponRefundFee;
    }


    public void setCouponRefundFee(int couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }


    public String getRefundStatus() {
        return this.refundStatus;
    }


    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }


    public String toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Field[] fields = getClass().getDeclaredFields();
        StringBuilder s = new StringBuilder("{");

        for (Field field : fields) {

            try {
                Object obj = field.get(this);
                if (obj != null) {
                    if (s.length() > 0) {
                        s.append(" ");
                    }
                    s.append(field.getName());
                    s.append("=");
                    s.append(obj.toString());
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        s.append("}");
        return s.toString();
    }
}



