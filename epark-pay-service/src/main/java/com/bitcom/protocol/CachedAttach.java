package com.bitcom.protocol;


public class CachedAttach {
    private PayInfo payInfo;
    private Object attach;

    public Object getAttach() {
        return this.attach;
    }


    public void setAttach(Object attach) {
        this.attach = attach;
    }


    public PayInfo getPayInfo() {
        return this.payInfo;
    }


    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }
}



