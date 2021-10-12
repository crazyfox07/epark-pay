package com.bitcom.pay.alipay.service;

import javax.servlet.http.HttpServletRequest;

public interface IAlipayCallbackService {
    String alipayCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



