package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;


public class BaseService {
    private String apiURL;
    private IServiceRequest serviceRequest;

    public BaseService(String api) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.apiURL = api;
        Class<?> c = Class.forName(Configure.HttpsRequestClassName);
        this.serviceRequest = (IServiceRequest) c.newInstance();
    }


    protected String sendPost(Object xmlObj) throws UnrecoverableKeyException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.serviceRequest.sendPost(this.apiURL, xmlObj);
    }


    public void setServiceRequest(IServiceRequest request) {
        this.serviceRequest = request;
    }
}



