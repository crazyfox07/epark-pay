package com.bitcom.sdk.wechat.business;

import com.bitcom.sdk.wechat.common.Log;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseReqData;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseResData;
import com.bitcom.sdk.wechat.service.ReverseService;
import org.slf4j.LoggerFactory;


public class ReverseBusiness {
    private static Log log = new Log(LoggerFactory.getLogger(RefundBusiness.class));

    private ReverseService reverseService;
    private boolean needRecallReverse = false;
    private int waitingTimeBeforeReverseServiceInvoked = 5000;
    private int retryTimes = 5;

    private ResultListener resultListener;

    public ReverseBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.reverseService = new ReverseService();
    }


    public void run(ReverseReqData reverseReqData, ResultListener resultListener) throws Exception {
        this.resultListener = resultListener;
        doReverseLoop(reverseReqData);
    }


    private boolean doOneReverse(ReverseReqData reverseReqData) throws Exception {
        Thread.sleep(this.waitingTimeBeforeReverseServiceInvoked);

        String reverseResponseString = this.reverseService.request(reverseReqData);

        log.i("撤销API返回的数据如下：");
        log.i(reverseResponseString);

        ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
        if (reverseResData == null) {
            log.i("支付订单撤销请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            return false;
        }
        if (reverseResData.getReturn_code().equals("FAIL")) {

            log.i("支付订单撤销API系统返回失败，失败信息为：" + reverseResData.getReturn_msg());
            return false;
        }
        if (reverseResData.getResult_code().equals("FAIL")) {
            log.i("撤销出错，错误码：" + reverseResData.getErr_code() + "     错误信息：" + reverseResData.getErr_code_des());
            if (reverseResData.getRecall().equals("Y")) {

                this.needRecallReverse = true;
                return false;
            }

            this.needRecallReverse = false;
            return true;
        }


        log.i("支付订单撤销成功");
        return true;
    }


    private void doReverseLoop(ReverseReqData reverseReqData) throws Exception {
        boolean res = false;
        this.retryTimes--;

        this.needRecallReverse = true;

        while (this.needRecallReverse && this.retryTimes > 0) {
            if (doOneReverse(reverseReqData)) {
                res = true;
            }
        }
        if (res) {
            this.resultListener.onReverseSuccess();
            log.i("撤销成功。");
        } else {
            this.resultListener.onReverseFail();
            log.i("撤销失败。trade_no" + reverseReqData.getOut_trade_no());
        }
    }


    public ReverseService getReverseService() {
        return this.reverseService;
    }


    public void setReverseService(ReverseService reverseService) {
        this.reverseService = reverseService;
    }

    public static interface ResultListener {
        void onReverseFail();

        void onReverseSuccess();
    }
}



