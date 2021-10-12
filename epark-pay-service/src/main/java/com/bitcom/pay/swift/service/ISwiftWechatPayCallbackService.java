package com.bitcom.pay.swift.service;

import javax.servlet.http.HttpServletRequest;

public interface ISwiftWechatPayCallbackService {
    String wechatCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



