package com.bitcom.pay.ccbpay.service;

import javax.servlet.http.HttpServletRequest;

public interface ICcbPayCallbackService {
    String ccbPayCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}
