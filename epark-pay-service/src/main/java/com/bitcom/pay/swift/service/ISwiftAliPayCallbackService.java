package com.bitcom.pay.swift.service;

import javax.servlet.http.HttpServletRequest;

public interface ISwiftAliPayCallbackService {
    String alipayCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



