package com.bitcom.pay.alipay.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.bitcom.api.service.ITradeFlowService;
import com.bitcom.base.domain.InfoZfbPay;
import com.bitcom.base.mapper.InfoZfbPayMapper;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.config.AlipayConfig;
import com.bitcom.pay.alipay.service.IAlipayCallbackService;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.alipay.util.AlipayUtil;
import com.bitcom.sdk.alipay.vo.AlipayNotifyParam;

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
public class AlipayCallbackServiceImpl
        implements IAlipayCallbackService {
    private Logger logger = LoggerFactory.getLogger(AlipayCallbackServiceImpl.class);


    @Autowired
    private InfoZfbPayMapper zfbPayMapper;

    @Autowired
    private ITradeFlowService tradeFlowService;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public static final String PAY_SCHEME = "0603";

    public static final String PAY_TYPE = "0805";


    @Transactional(rollbackFor = {Exception.class})
    public String alipayCallbackHandler(HttpServletRequest request) throws Exception {
        try {
            Map<String, String> paramsMap = AlipayUtil.convertRequestParamsToMap(request);
            String jsonStr = JSON.toJSONString(paramsMap);
            this.logger.info("【支付宝回调】:{}", jsonStr);
            AlipayNotifyParam notifyParam = (AlipayNotifyParam) JSON.parseObject(jsonStr, AlipayNotifyParam.class);

            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.ZFB_PUBLIC_KEY_RSA2, "utf-8", "RSA2");
            this.logger.info("【支付宝回调】sign_type={},signCheck res={}", notifyParam.getSignType(), Boolean.valueOf(signVerified));

            if (!signVerified) {
                this.logger.error("【支付宝回调】验签失败");
                return "failure";
            }

            String outTradeNo = notifyParam.getOutTradeNo();

            List<InfoZfbPay> cachedPrePayList = this.zfbPayMapper.queryZfbPayByOutTradeNo(outTradeNo);

            if (cachedPrePayList.size() == 0) {
                this.logger.error("【支付宝回调】cached prePay was not found. maybe attacked.");
                return "failure";
            }
            InfoZfbPay cachePay = cachedPrePayList.get(0);


            String sellerId = notifyParam.getSellerId();
            if (!StringUtils.equals(sellerId, AlipayConfig.PARTNER_ID)) {
                this.logger.error("【支付宝回调】sellerId = {} was not found", sellerId);
                return "failure";
            }


            String tradeStatus = notifyParam.getTradeStatus();
            if (StringUtils.equals(tradeStatus, "TRADE_SUCCESS")||StringUtils.equals(tradeStatus, "TRADE_FINISHED")) {

                int count = updateInfoZfbPayByNotifyParam(notifyParam);
                if (count == 0) {
                    this.logger.error("【支付宝回调】更新缓存表失败，可能已回调处理，注意分析，这里返回success");
                    return "success";
                }

                CachedAttach cachedAttach = (CachedAttach) JSON.parseObject(cachePay.getAttach(), CachedAttach.class);

                String tradeNo = notifyParam.getTradeNo();
                String total = notifyParam.getTotalAmount();
                Date gmtPay = DateUtils.parseDate(notifyParam.getGmtPayment(), "yyyy-MM-dd HH:mm:ss");
                String buyer = notifyParam.getBuyerId();
                int c = this.tradeFlowService.saveTradeFlow(tradeNo, outTradeNo, "0603", "0805", "", total, gmtPay, buyer);
                this.logger.info("【支付宝回调】save trade flow, res = {}", Integer.valueOf(c));


                String tag = cachedAttach.getPayInfo().getBizName();
                PayMessage payMessage = new PayMessage();
                payMessage.setOutTradeNo(outTradeNo);
                payMessage.setBizName(tag);
                payMessage.setPayType("0805");
                payMessage.setPayScheme("0603");
                payMessage.setTotalAmount(total);
                payMessage.setPayTime(notifyParam.getGmtPayment());
                payMessage.setAttach(cachedAttach.getAttach());

                Message msg = new Message("topic-pay", tag, JSON.toJSONString(payMessage).getBytes());
                SendResult sendResult = this.defaultMQProducer.send(msg);
                if (sendResult != null) {
                    this.logger.info("【支付宝回调】发送消息成功.Topic={},msgId={}", msg.getTopic(), sendResult.getMsgId());
                }
            } else {

                this.logger.error("【支付宝回调】未考虑的状态 tradeStatus = {}", tradeStatus);
            }
            return "success";
        } catch (Exception e) {
            this.logger.error("【支付宝回调】Exception.{}", e.getMessage());
            throw e;
        }
    }


    private int updateInfoZfbPayByNotifyParam(AlipayNotifyParam notifyParam) {
        List<InfoZfbPay> cachedPrePayList = this.zfbPayMapper.queryZfbPayByOutTradeNo(notifyParam.getOutTradeNo());
        InfoZfbPay cachePay = cachedPrePayList.get(0);

        cachePay.setNotifyTime(notifyParam.getNotifyTime());
        cachePay.setNotifyType(notifyParam.getNotifyType());
        cachePay.setNotifyId(notifyParam.getNotifyId());
        cachePay.setSignType(notifyParam.getSignType());
        cachePay.setSign(notifyParam.getSign());
        cachePay.setSubject(notifyParam.getSubject());
        cachePay.setTradeNo(notifyParam.getTradeNo());
        cachePay.setTradeStatus(notifyParam.getTradeStatus());
        cachePay.setSellerId(notifyParam.getSellerId());
        cachePay.setBuyerId(notifyParam.getBuyerId());
        cachePay.setTotalFee(notifyParam.getTotalAmount());
        cachePay.setBody(notifyParam.getBody());
        cachePay.setGmtCreate(notifyParam.getGmtCreate());
        cachePay.setGmtPayment(notifyParam.getGmtPayment());

        return this.zfbPayMapper.updateCallBackZfbPay(cachePay);
    }
}



