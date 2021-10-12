package com.bitcom.sdk.wechat;

import com.bitcom.sdk.wechat.business.DownloadBillBusiness;
import com.bitcom.sdk.wechat.business.RefundBusiness;
import com.bitcom.sdk.wechat.business.RefundQueryBusiness;
import com.bitcom.sdk.wechat.business.ReverseBusiness;
import com.bitcom.sdk.wechat.business.ScanPayBusiness;
import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.downloadbill_protocol.DownloadBillReqData;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayReqData;
import com.bitcom.sdk.wechat.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.bitcom.sdk.wechat.protocol.refund_protocol.RefundReqData;
import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundQueryReqData;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseReqData;
import com.bitcom.sdk.wechat.service.DownloadBillService;
import com.bitcom.sdk.wechat.service.RefundQueryService;
import com.bitcom.sdk.wechat.service.RefundService;
import com.bitcom.sdk.wechat.service.ReverseService;
import com.bitcom.sdk.wechat.service.ScanPayQueryService;
import com.bitcom.sdk.wechat.service.ScanPayService;


public class WXPay {
    public static void initSDKConfiguration(String key, String appID, String mchID, String sdbMchID, String certLocalPath, String certPassword) {
        Configure.setKey(key);
        Configure.setAppID(appID);
        Configure.setMchID(mchID);
        Configure.setSubMchID(sdbMchID);
        Configure.setCertLocalPath(certLocalPath);
        Configure.setCertPassword(certPassword);
    }


    public static String requestScanPayService(ScanPayReqData scanPayReqData) throws Exception {
        return (new ScanPayService()).request(scanPayReqData);
    }


    public static String requestScanPayQueryService(ScanPayQueryReqData scanPayQueryReqData) throws Exception {
        return (new ScanPayQueryService()).request(scanPayQueryReqData);
    }


    public static String requestRefundService(RefundReqData refundReqData) throws Exception {
        return (new RefundService()).request(refundReqData);
    }


    public static String requestRefundQueryService(RefundQueryReqData refundQueryReqData) throws Exception {
        return (new RefundQueryService()).request(refundQueryReqData);
    }


    public static String requestReverseService(ReverseReqData reverseReqData) throws Exception {
        return (new ReverseService()).request(reverseReqData);
    }


    public static String requestDownloadBillService(DownloadBillReqData downloadBillReqData) throws Exception {
        return (new DownloadBillService()).request(downloadBillReqData);
    }


    public static void doScanPayBusiness(ScanPayReqData scanPayReqData, ScanPayBusiness.ResultListener resultListener) throws Exception {
        (new ScanPayBusiness()).run(scanPayReqData, resultListener);
    }


    public static void doReverseBusiness(ReverseReqData reverseReqData, ReverseBusiness.ResultListener resultListener) throws Exception {
        (new ReverseBusiness()).run(reverseReqData, resultListener);
    }


    public static void doRefundBusiness(RefundReqData refundReqData, RefundBusiness.ResultListener resultListener) throws Exception {
        (new RefundBusiness()).run(refundReqData, resultListener);
    }


    public static void doRefundQueryBusiness(RefundQueryReqData refundQueryReqData, RefundQueryBusiness.ResultListener resultListener) throws Exception {
        (new RefundQueryBusiness()).run(refundQueryReqData, resultListener);
    }


    public static void doDownloadBillBusiness(DownloadBillReqData downloadBillReqData, DownloadBillBusiness.ResultListener resultListener) throws Exception {
        (new DownloadBillBusiness()).run(downloadBillReqData, resultListener);
    }
}



