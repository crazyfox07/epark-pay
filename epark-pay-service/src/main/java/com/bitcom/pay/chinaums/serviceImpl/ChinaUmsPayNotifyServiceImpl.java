package com.bitcom.pay.chinaums.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.domain.InfoUmsPay;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.base.mapper.InfoUmsPayMapper;
import com.bitcom.common.utils.DateUtils;
import com.bitcom.common.utils.NumberUtils;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.config.ChinaUmsConfig;
import com.bitcom.pay.chinaums.service.IChinaUmsPayNotifyService;
import com.bitcom.protocol.CachedAttach;
import com.bitcom.protocol.PayMessage;
import com.bitcom.sdk.chinaums.ChinaUmsRequest;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChinaUmsPayNotifyServiceImpl
        implements IChinaUmsPayNotifyService {
    private static Logger logger = LoggerFactory.getLogger(ChinaUmsPayNotifyServiceImpl.class);

    public static final String PAY_SCHEME = "0605";

    @Autowired
    private InfoUmsPayMapper infoUmsPayMapper;

    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Autowired
    private DefaultMQProducer defaultMQProducer;


    public String chinaUmsPayCallbackHandler(HttpServletRequest request) throws Exception {
        ChinaUmsRequest resHandler = new ChinaUmsRequest(request);
        resHandler.setKey(ChinaUmsConfig.key);
        logger.info("【银联商务支付回调】:{}", JSON.toJSONString(resHandler.getAllParameters()));


        try {
            String status = resHandler.getParameter("status");
            if (!"TRADE_SUCCESS".equals(status)) {
                return "FAILED";
            }


            if (!resHandler.isValidSign()) {
                logger.error("chinaUms payment notify sign validate failure.");
                return "FAILED";
            }


            String merOrderId = resHandler.getParameter("merOrderId");
            InfoUmsPay pay = this.infoUmsPayMapper.getUmsPayByMerOrderId(merOrderId);
            if (pay == null) {
                logger.error("Unify pay pay was not found. maybe attacked!");
                return "FAILED";
            }


            String targetSys = resHandler.getParameter("targetSys");
            String totalAmount = resHandler.getParameter("invoiceAmount");
            String buyer = resHandler.getParameter("buyerId");
            String payTime = resHandler.getParameter("payTime");
            Date gmtPay = DateUtils.parseDate(payTime, "yyyy-MM-dd HH:mm:ss");
            Double total = Double.valueOf(NumberUtils.doubleRoundTwoBit(Double.valueOf(totalAmount).doubleValue() / 100.0D));
            String mid = resHandler.getParameter("mid");
            String tid = resHandler.getParameter("tid");
            pay.setMid(mid);
            pay.setTid(tid);
            pay.setBuyerId(buyer);
            pay.setNotifyId(resHandler.getParameter("notifyId"));
            pay.setBuyerName(resHandler.getParameter("buyerName"));
            pay.setBillFunds(resHandler.getParameter("billFunds"));
            pay.setBillFundsDesc(resHandler.getParameter("billFundsDesc"));
            pay.setTotalAmount(Integer.valueOf(totalAmount));
            pay.setInvoiceAmount(Integer.valueOf(resHandler.getParameter("invoiceAmount")));
            pay.setPayTime(payTime);
            pay.setSettleDate(resHandler.getParameter("settleDate"));
            pay.setStatus(resHandler.getParameter("status"));
            pay.setTargetOrderId(resHandler.getParameter("targetOrderId"));
            pay.setTargetSys(targetSys);
            pay.setSign(resHandler.getParameter("sign"));

            int count = this.infoUmsPayMapper.updateCallbackPay(pay);

            if (count == 0) {
                logger.error("Ums callback pay update failure.please check the log. merOrderId={}", merOrderId);
                return "SUCCESS";
            }


            String payType = "0805";
            if (StringUtils.contains(targetSys, "WXPay")) {
                payType = "0804";
            }
            InfoTradeFlow flow = new InfoTradeFlow();
            flow.setNid(UUIDUtils.getUUID());
            flow.setTradeNo(merOrderId);
            flow.setOutTradeNo(merOrderId);
            flow.setMerId(mid);
            flow.setTermimalId(tid);
            flow.setPayScheme("0605");
            flow.setPayType(payType);
            flow.setTotalAmount(String.valueOf(total));
            flow.setGmtPayment(gmtPay);
            flow.setBuyerId(buyer);
            int c = this.infoTradeFlowMapper.insert(flow);
            logger.info("chinaUms save trade flow, res = {}", Integer.valueOf(c));


            CachedAttach cachedAttach = (CachedAttach) JSON.parseObject(pay.getAttach(), CachedAttach.class);
            String tag = cachedAttach.getPayInfo().getBizName();
            PayMessage payMessage = new PayMessage();
            payMessage.setOutTradeNo(merOrderId);
            payMessage.setBizName(tag);
            payMessage.setPayScheme("0605");
            payMessage.setPayType(payType);
            payMessage.setTotalAmount(String.valueOf(total));
            payMessage.setPayTime(DateUtils.toTimeStr(gmtPay));
            payMessage.setAttach(cachedAttach.getAttach());

            Message msg = new Message("topic-pay", tag, JSON.toJSONString(payMessage).getBytes());
            SendResult sendResult = this.defaultMQProducer.send(msg);
            if (sendResult != null) {
                logger.info("chinaUms pay callback message send success.Topic={},msgId={}", msg.getTopic(), sendResult.getMsgId());
            }
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}



