package com.bitcom.pay.chinaums.service;

import javax.servlet.http.HttpServletRequest;

public interface IChinaUmsPayNotifyService {
    String chinaUmsPayCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



