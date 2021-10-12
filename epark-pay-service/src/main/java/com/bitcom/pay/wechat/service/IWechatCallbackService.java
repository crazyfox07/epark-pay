package com.bitcom.pay.wechat.service;

import javax.servlet.http.HttpServletRequest;

public interface IWechatCallbackService {
    String wechatCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



