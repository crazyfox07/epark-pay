package com.bitcom.pay.swift.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoWxPay;
import com.bitcom.base.mapper.InfoWxPayMapper;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.config.SwiftConfig;
import com.bitcom.pay.swift.service.ISwiftWechatPayCallbackService;
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


@Transactional
@Service
public class SwiftWechatCallbackServiceImpl
        implements ISwiftWechatPayCallbackService {
    public static final Logger logger = LoggerFactory.getLogger(SwiftWechatCallbackServiceImpl.class);

    @Autowired
    private InfoWxPayMapper infoWxPayMapper;

    @Autowired
    private ITradeFlowService tradeFlowService;

    @Autowired
    private DefaultMQProducer defaultMQProducer;


    @Transactional(rollbackFor = {Exception.class})
    public String wechatCallbackHandler(HttpServletRequest request) throws Exception {
        SwiftRequestHandler resHandler = new SwiftRequestHandler(request);
        logger.info("【Swift微信支付回调】:{}", JSON.toJSONString(resHandler.getAllParameters()));
        resHandler.setKey(SwiftConfig.swift_public_key);

        try {
            if (!resHandler.validSign()) {
                logger.error("【Swift微信支付回调】验签失败");
                return "fail";
            }

            String status = resHandler.getParameter("status");
            if (!StringUtils.equals("0", status)) {
                logger.error("【Swift微信支付回调】失败. err_msg={}", resHandler.getParameter("err_msg"));
                return "fail";
            }

            String resCode = resHandler.getParameter("result_code");
            if (!StringUtils.equals("0", resCode)) {
                logger.error("【Swift微信支付回调】失败。 resCode={}", resCode);
                return "fail";
            }


            String outTradeNo = resHandler.getParameter("out_trade_no");
            List<InfoWxPay> cachedPrePayList = this.infoWxPayMapper.queryWxPayByOutTradeNo(outTradeNo);
            if (cachedPrePayList.size() == 0) {
                logger.error("【Swift微信支付回调】cached prePay was not found. maybe attacked.");
                return "fail";
            }


            InfoWxPay cachePay = cachedPrePayList.get(0);
            int count = updateInfoWxPayByNotifyParam(resHandler.getAllParameters());
            if (count == 0) {
                logger.error("【Swift微信支付回调】重大Exception(事务没回滚或重复下发)这里只能返回success。调查该问题，首先得确认充值是否到账了，如果到账万事大吉。（rocketMQ虽然报异常，但实际发送成功）没到账，检查支付缓存表以及流水表，若已经写入了，说明问题很严重，确实没有回滚,让研发人肉运维补上。（需要修改订单，人工写入支付流水，补平订单即可）");


                return "success";
            }


            String tradeNo = resHandler.getParameter("transaction_id");
            String fee = resHandler.getParameter("total_fee");
            Double totalAmount = Double.valueOf(Double.valueOf(fee).doubleValue() / 100.0D);
            String total = Double.toString(NumberUtils.doubleRoundTwoBit(totalAmount.doubleValue()));
            Date gmtPay = DateUtils.parseDate(resHandler.getParameter("time_end"), "yyyyMMddHHmmss");
            String buyer = resHandler.getParameter("sub_openid");
            int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, "0604", "0804", "", total, gmtPay, buyer);
            logger.info("【Swift微信支付回调】save trade flow, res = {}", Integer.valueOf(c));


            CachedAttach cachedAttach = (CachedAttach) JSON.parseObject(cachePay.getAttach(), CachedAttach.class);
            String tag = cachedAttach.getPayInfo().getBizName();
            PayMessage payMessage = new PayMessage();
            payMessage.setOutTradeNo(outTradeNo);
            payMessage.setBizName(tag);
            payMessage.setPayScheme("0604");
            payMessage.setPayType("0804");
            payMessage.setTotalAmount(total);
            payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
            payMessage.setAttach(cachedAttach.getAttach());

            Message msg = new Message("topic-pay", tag, JSON.toJSONString(payMessage).getBytes());
            SendResult sendResult = this.defaultMQProducer.send(msg);
            if (sendResult != null) {
                logger.info("【Swift微信支付回调】发送消息成功.Topic={},msgId={}", msg.getTopic(), sendResult.getMsgId());
            }
            return "success";
        } catch (Exception e) {
            logger.error("【Swift微信支付回调】Exception，{}", e.getMessage());
            throw e;
        }
    }


    private int updateInfoWxPayByNotifyParam(Map<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        List<InfoWxPay> cachedPrePayList = this.infoWxPayMapper.queryWxPayByOutTradeNo(outTradeNo);
        InfoWxPay wxPay = cachedPrePayList.get(0);

        wxPay.setReturnCode(params.get("status"));
        wxPay.setReturnMsg(params.get("message"));
        wxPay.setAppid(params.get("sub_appid"));
        wxPay.setMchId(params.get("mch_id"));
        wxPay.setDeviceInfo(params.get("device_info"));
        wxPay.setNonceStr(params.get("nonce_str"));
        wxPay.setResultCode(params.get("result_code"));
        wxPay.setErrCode(params.get("err_code"));
        wxPay.setErrCodeDes(params.get("err_msg"));
        wxPay.setOpenid(params.get("sub_openid"));
        wxPay.setIsSubscribe(params.get("sub_is_subscribe"));
        wxPay.setTradeType(params.get("trade_type"));
        wxPay.setBankType(params.get("bank_type"));
        wxPay.setTotalFee(params.get("total_fee"));
        wxPay.setFeeType(params.get("fee_type"));
        wxPay.setCashFee(params.get("cash_fee"));
        wxPay.setCouponFee(params.get("coupon_fee"));
        wxPay.setTransactionId(params.get("transaction_id"));
        wxPay.setTimeEnd(params.get("time_end"));

        return this.infoWxPayMapper.updateCallBackWxPay(wxPay);
    }
}



