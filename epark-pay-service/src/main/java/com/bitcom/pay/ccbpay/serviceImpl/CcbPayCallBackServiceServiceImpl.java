package com.bitcom.pay.ccbpay.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoCcbPay;
import com.bitcom.base.domain.InfoWxPay;
import com.bitcom.base.mapper.InfoCcbPayMapper;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.common.utils.Topics;
import com.bitcom.config.CcbConfig;
import com.bitcom.config.WechatConfig;
import com.bitcom.pay.ccbpay.service.ICcbPayCallbackService;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.wechat.ResponseHandler;
import com.bitcom.sdk.wechat.util.TenpayUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Service
public class CcbPayCallBackServiceServiceImpl implements ICcbPayCallbackService {
    private Logger logger = LoggerFactory.getLogger(CcbPayCallBackServiceServiceImpl.class);

    private final String payScheme = CcbConfig.PAY_SCHEME;
    private final String payType = CcbConfig.PAY_TYPE;

    @Autowired
    private ITradeFlowService tradeFlowService;

    @Autowired
    private InfoCcbPayMapper infoCcbPayMapper;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public String ccbPayCallbackHandler(HttpServletRequest request) throws Exception {
        try {
//            Enumeration<String> e = request.getHeaderNames();
//            while(e.hasMoreElements()){
//                String headerName = e.nextElement();//透明称
//                Enumeration<String> headerValues = request.getHeaders(headerName);
//                while(headerValues.hasMoreElements()){
//                    System.out.println(headerName+":"+headerValues.nextElement());
//                }
//            }
            this.logger.info("【建行龙支付回调参数】:{}", request.getQueryString());
            String tradeNo = TenpayUtil.createOutTradeNo();
            String outTradeNo = request.getParameter("ORDERID");
            String payApiChannel = "1002";
            String total = request.getParameter("PAYMENT");
            // todo 待删
//            total = "0.01";
            Date gmtPay = new Date();
            // todo 待修改
            String buyer =  TenpayUtil.createOutTradeNo();
            // 通过订单号outTradeNo查询info_ccb_pay表
            List<InfoCcbPay> cachedPrePayList = this.infoCcbPayMapper.queryCcbPayByOutTradeNo(outTradeNo);

            if (cachedPrePayList.size() == 0) {
                this.logger.error("【建行龙支付回调】cached prePay was not found. maybe attacked.");
                return ResponseHandler.fail();
            }

            InfoCcbPay cachePay = cachedPrePayList.get(0);
            int count = updateInfoWxPayByNotifyParam(request);
            if (count == 0) {
                this.logger.error("【建行龙支付回调】重大Exception(事务没回滚或重复下发)这里只能返回success。调查该问题，首先得确认充值是否到账了，如果到账万事大吉。（rocketMQ虽然报异常，但实际发送成功）没到账，检查支付缓存表以及流水表，若已经写入了，说明问题很严重，确实没有回滚,让研发人肉运维补上");
                return ResponseHandler.success();
            }
            int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, this.payScheme, this.payType, payApiChannel, total, gmtPay, buyer);
            this.logger.info("【建行龙支付回调】save trade flow, res = {}", Integer.valueOf(c));

            CachedAttach cachedAttach = JSON.parseObject(cachePay.getAttach(), CachedAttach.class);
            String tag = cachedAttach.getPayInfo().getBizName();
            PayMessage payMessage = new PayMessage();
            payMessage.setOutTradeNo(outTradeNo);
            payMessage.setBizName(tag);
            payMessage.setPayScheme(this.payScheme);
            payMessage.setPayType(this.payType);
            payMessage.setTotalAmount(total);
            payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
            payMessage.setAttach(cachedAttach.getAttach());
            Message msg = new Message(Topics.TOPIC_PAY, tag, JSON.toJSONString(payMessage).getBytes());
            SendResult sendResult = this.defaultMQProducer.send(msg);
            if (sendResult != null) {
                this.logger.info("【建行龙支付回调】发送消息成功.Topic={},msgId={}", msg.getTopic(), sendResult.getMsgId());
            }
            System.out.println("*************************************************************************");
            return ResponseHandler.success();

        } catch (Exception e) {
            this.logger.error("【建行龙支付回调】Exception，{}", e.getMessage());
            throw e;
        }
    }

    private int updateInfoWxPayByNotifyParam(HttpServletRequest request) {
        String outTradeNo = request.getParameter("ORDERID");
        List<InfoCcbPay> cachedPrePayList = this.infoCcbPayMapper.queryCcbPayByOutTradeNo(outTradeNo);
        InfoCcbPay ccbPay = cachedPrePayList.get(0);

        ccbPay.setPosId(request.getParameter("POSID"));
        ccbPay.setBranchId(request.getParameter("BRANCHID"));
        ccbPay.setPayment(request.getParameter("PAYMENT"));
        ccbPay.setCurCode(request.getParameter("CURCODE"));
        ccbPay.setRemark1(request.getParameter("REMARK1"));
        ccbPay.setRemark2(request.getParameter("REMARK2"));
        ccbPay.setAccType(request.getParameter("ACC_TYPE"));
        ccbPay.setSuccess(request.getParameter("SUCCESS"));
        ccbPay.setType(request.getParameter("TYPE"));
        ccbPay.setUsrMsg("");
        ccbPay.setUsrInfo("");
        ccbPay.setSign(request.getParameter("SIGN"));

        return this.infoCcbPayMapper.updateCallBackCcbPay(ccbPay);
    }
}
