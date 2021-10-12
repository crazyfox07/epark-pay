package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseReqData;


public class ReverseService
        extends BaseService {
    public ReverseService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REVERSE_API);
    }


    public String request(ReverseReqData reverseReqData) throws Exception {
        String responseString = sendPost(reverseReqData);

        return responseString;
    }
}



