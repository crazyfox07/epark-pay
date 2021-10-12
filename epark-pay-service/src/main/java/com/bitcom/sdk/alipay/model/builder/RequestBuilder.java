package com.bitcom.sdk.alipay.model.builder;

import com.bitcom.sdk.alipay.util.GsonFactory;


public abstract class RequestBuilder {
    private String appAuthToken;
    private String notifyUrl;

    public abstract boolean validate();

    public abstract Object getBizContent();

    public String toJsonString() {
        return GsonFactory.getGson().toJson(getBizContent());
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("RequestBuilder{");
        sb.append("appAuthToken='").append(this.appAuthToken).append('\'');
        sb.append(", notifyUrl='").append(this.notifyUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public String getAppAuthToken() {
        return this.appAuthToken;
    }


    public RequestBuilder setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
        return this;
    }


    public String getNotifyUrl() {
        return this.notifyUrl;
    }


    public RequestBuilder setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }
}



