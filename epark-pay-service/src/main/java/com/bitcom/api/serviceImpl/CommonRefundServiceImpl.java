package com.bitcom.api.serviceImpl;

import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("commonRefundServiceImpl")
public class CommonRefundServiceImpl
        implements IRefundService {
    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;
    @Autowired
    @Qualifier("alipayRefundServiceImpl")
    private IRefundService alipayRefundServiceImpl;
    @Autowired
    @Qualifier("wechatPayRefundServiceImpl")
    private IRefundService wechatPayRefundServiceImpl;
    @Autowired
    @Qualifier("swiftRefundServiceImpl")
    private IRefundService swiftRefundServiceImpl;
    @Autowired
    @Qualifier("chinaUmsRefundServiceImpl")
    private IRefundService chinaUmsRefundServiceImpl;

    @Autowired
    @Qualifier("ccbPayRefundServiceImpl")
    private IRefundService ccbPayRefundServiceImpl;

    @Transactional(rollbackFor = {Exception.class})
    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);
        String payScheme = flow.getPayScheme();
        if (StringUtils.equals(payScheme, "0602"))
            return this.wechatPayRefundServiceImpl.refund(outTradeNo, outRefundNo, refundFee);
        if (StringUtils.equals(payScheme, "0603"))
            return this.alipayRefundServiceImpl.refund(outTradeNo, outRefundNo, refundFee);
        if (StringUtils.equals(payScheme, "0604"))
            return this.swiftRefundServiceImpl.refund(outTradeNo, outRefundNo, refundFee);
        if (StringUtils.equals(payScheme, "0605")) {
            return this.chinaUmsRefundServiceImpl.refund(outTradeNo, outRefundNo, refundFee);
        }
        if (StringUtils.equals(payScheme, "0611")){
            return this.ccbPayRefundServiceImpl.refund(outTradeNo, outRefundNo, refundFee);
        }
        return null;
    }
}



