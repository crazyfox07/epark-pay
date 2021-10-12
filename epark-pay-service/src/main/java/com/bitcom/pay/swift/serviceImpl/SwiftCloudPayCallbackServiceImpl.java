package com.bitcom.pay.swift.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoSwiftcloudPay;
import com.bitcom.base.mapper.InfoSwiftcloudPayMapper;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.SwiftConfig;
import com.bitcom.pay.swift.service.ISwiftCloudPayCallbackService;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.swfit.wechat.SwiftRequestHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SwiftCloudPayCallbackServiceImpl
        implements ISwiftCloudPayCallbackService {
    public static final Logger logger = LoggerFactory.getLogger(SwiftCloudPayCallbackServiceImpl.class);

    @Autowired
    private InfoSwiftcloudPayMapper infoSwiftcloudPayMapper;

    @Autowired
    private ITradeFlowService tradeFlowService;

    @Autowired
    private DefaultMQProducer defaultMQProducer;


    @Transactional(rollbackFor = {Exception.class})
    public String cloudPayCallbackHandler(HttpServletRequest request) throws Exception {
        SwiftRequestHandler resHandler = new SwiftRequestHandler(request);
        logger.info("【Swift银联JS支付回调】:{}", JSON.toJSONString(resHandler.getAllParameters()));
        resHandler.setKey(SwiftConfig.swift_public_key);

        try {
            if (!resHandler.validSign()) {
                logger.error("【Swift银联JS支付回调】验签失败");
                return "fail";
            }

            String status = resHandler.getParameter("status");
            if (!StringUtils.equals("0", status)) {
                logger.error("【Swift银联JS支付回调】失败. err_msg={}", resHandler.getParameter("err_msg"));
                return "fail";
            }

            String resCode = resHandler.getParameter("result_code");
            if (!StringUtils.equals("0", resCode)) {
                logger.error("【Swift银联JS支付回调】失败。 resCode={}", resCode);
                return "fail";
            }


            String outTradeNo = resHandler.getParameter("out_trade_no");
            List<InfoSwiftcloudPay> cachedPrePayList = this.infoSwiftcloudPayMapper.queryPayInfoByOutTradeNo(outTradeNo);
            if (cachedPrePayList.size() == 0) {
                logger.error("【Swift银联JS支付回调】cached prePay was not found. maybe attacked.");
                return "fail";
            }


            InfoSwiftcloudPay cachePay = cachedPrePayList.get(0);
            int count = updatePayInfoByNotifyParam(resHandler.getAllParameters());

            if (count == 0) {
                logger.error("【Swift银联JS支付回调】重大Exception(事务没回滚或重复下发)这里只能返回success。调查该问题，首先得确认充值是否到账了，如果到账万事大吉。（rocketMQ虽然报异常，但实际发送成功）没到账，检查支付缓存表以及流水表，若已经写入了，说明问题很严重，确实没有回滚,让研发人肉运维补上。（需要修改订单，人工写入支付流水，补平订单即可）");


                return "success";
            }


            String tradeNo = resHandler.getParameter("transaction_id");
            String fee = resHandler.getParameter("total_fee");
            Double totalAmount = Double.valueOf(Double.valueOf(fee).doubleValue() / 100.0D);
            String total = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            Date gmtPay = DateUtils.parseDate(resHandler.getParameter("time_end"), "yyyyMMddHHmmss");
            int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, "0604", "0806", "", total, gmtPay, "");
            logger.info("【Swift银联JS支付回调】save trade flow, res = {}", Integer.valueOf(c));


            CachedAttach cachedAttach = (CachedAttach) JSON.parseObject(cachePay.getAttach(), CachedAttach.class);
            String tag = cachedAttach.getPayInfo().getBizName();
            PayMessage payMessage = new PayMessage();
            payMessage.setOutTradeNo(outTradeNo);
            payMessage.setBizName(tag);
            payMessage.setPayScheme("0604");
            payMessage.setPayType("0806");
            payMessage.setTotalAmount(total);
            payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
            payMessage.setAttach(cachedAttach.getAttach());

            Message msg = new Message("topic-pay", tag, JSON.toJSONString(payMessage).getBytes());
            SendResult sendResult = this.defaultMQProducer.send(msg);
            if (sendResult != null) {
                logger.info("【Swift银联JS支付回调】发送消息成功.Topic={},msgId={}", msg.getTopic(), sendResult.getMsgId());
            }
            return "success";
        } catch (Exception e) {
            logger.error("【Swift银联JS支付回调】Exception，{}", e.getMessage());
            throw e;
        }
    }

    private int updatePayInfoByNotifyParam(Map<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        List<InfoSwiftcloudPay> cachedPrePayList = this.infoSwiftcloudPayMapper.queryPayInfoByOutTradeNo(outTradeNo);
        InfoSwiftcloudPay pay = cachedPrePayList.get(0);

        pay.setStatus(params.get("status"));
        pay.setResultCode(params.get("result_code"));
        pay.setMchId(params.get("mch_id"));
        pay.setSign(params.get("sign"));
        pay.setTradeType(params.get("trade_type"));
        pay.setPayResult(Integer.valueOf(params.get("pay_result")));
        pay.setTransactionId(params.get("transaction_id"));
        pay.setOutTransactionId(params.get("out_transaction_id"));
        pay.setTotalFee(Integer.valueOf(params.get("total_fee")));
        pay.setBankType(params.get("bank_type"));
        pay.setTimeEnd(params.get("time_end"));

        return this.infoSwiftcloudPayMapper.updateCallBackPayInfo(pay);
    }
}



