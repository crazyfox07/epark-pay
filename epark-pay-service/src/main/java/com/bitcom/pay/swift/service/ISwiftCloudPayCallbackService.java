package com.bitcom.pay.swift.service;

import javax.servlet.http.HttpServletRequest;

public interface ISwiftCloudPayCallbackService {
    String cloudPayCallbackHandler(HttpServletRequest paramHttpServletRequest) throws Exception;
}



