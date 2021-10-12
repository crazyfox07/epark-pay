package com.bitcom.api.serviceImpl;

import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.utils.UUIDUtils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TradeFlowServiceImpl
        implements ITradeFlowService {
    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Transactional(rollbackFor = {Exception.class})
    public int saveTradeFlow(String tradeNo, String outTradeNo, String payScheme, String payType, String payApiChannel, String totalAmount, Date gmtPayment, String buyerId) throws Exception {
        InfoTradeFlow flow = new InfoTradeFlow();
        flow.setNid(UUIDUtils.getUUID());
        flow.setTradeNo(tradeNo);
        flow.setOutTradeNo(outTradeNo);
        flow.setPayScheme(payScheme);
        flow.setPayType(payType);
        flow.setPayApiChannel(payApiChannel);
        flow.setTotalAmount(totalAmount);
        flow.setGmtPayment(gmtPayment);
        flow.setBuyerId(buyerId);
        return this.infoTradeFlowMapper.insert(flow);
    }
}



