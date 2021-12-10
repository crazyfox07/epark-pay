package com.bitcom.pay.ccbpay.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.api.service.IRefundService;
import com.bitcom.base.domain.InfoRefundFlow;
import com.bitcom.base.domain.InfoTradeFlow;
import com.bitcom.base.mapper.InfoRefundFlowMapper;
import com.bitcom.base.mapper.InfoTradeFlowMapper;
import com.bitcom.common.ReturnObject;
import com.bitcom.common.utils.UUIDUtils;
import com.bitcom.config.WechatConfig;
import com.bitcom.sdk.wechat.PrepayIdRequestHandler;
import com.bitcom.sdk.wechat.ResponseHandler;
import com.bitcom.sdk.wechat.util.HttpsRequest;
import com.bitcom.sdk.wechat.util.WXUtil;
import org.apache.commons.lang3.StringUtils;
import com.bitcom.config.CcbConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.CubicCurve2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

@Service("ccbPayRefundServiceImpl")
public class CcbPayRefundServiceImpl implements IRefundService {
    private Logger logger = LoggerFactory.getLogger(CcbPayRefundServiceImpl.class);

    @Autowired
    private InfoTradeFlowMapper infoTradeFlowMapper;

    @Autowired
    private InfoRefundFlowMapper infoRefundFlowMapper;

    /**
     * 建行退费
     * @param ipAdress
     * @param nPort
     * @param sRequest
     * @return
     */
    public String ccbRefund(String ipAdress, int nPort, String sRequest){
        String sResult = "";
        OutputStream out = null;
        BufferedReader in = null;
        try{
            String encoding = "GB18030";
            String params = "requestXml=" + URLEncoder.encode(sRequest, encoding);
            String path = "http://" + ipAdress + ":" + nPort;
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置HTTP请求报文头
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
            conn.setRequestProperty("Connection", "close");

            // 发送请求报文数据
            out = conn.getOutputStream();
            out.write(params.getBytes(encoding));
            out.flush();
            out.close();

            // 读取返回数据
            if (conn.getResponseCode() == 200){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), encoding));
            }

            String sLine = null;
            StringBuffer sb = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                sb.append(sLine);
            }
            sResult = sb.toString();


        } catch (Exception e){
            logger.error("建行退款失败： {}", e.getMessage(), e);
        } finally {
            try{
                if(in != null){
                    in.close();
                }
                if(out != null){
                    out.close();
                }
            } catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        return sResult;
    }

    /**
     *
     * @param xmlStr
     */
    public JSONObject parseXml(String xmlStr){
        JSONObject resultJson = new JSONObject();
        //1.创建Reader对象
        try {
            //2.加载xml
            Document document = DocumentHelper.parseText(xmlStr);
            //3.获取根节点
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext()){
                Element iterator1 = (Element) iterator.next();
                resultJson.put(iterator1.getName(), iterator1.getStringValue());
                Iterator iterator2 = iterator1.elementIterator();
                while (iterator2.hasNext()){
                    Element iterator3 = (Element) iterator2.next();
                    resultJson.put(iterator3.getName(), iterator3.getStringValue());
                }
            }
            logger.info("建行退款接口返回： " + resultJson.toJSONString());
        } catch (Exception e) {
            logger.error("建行退款解析xml文件异常： {}", e.getMessage(), e);
        }
        return resultJson;
    }

    @Override
    public ReturnObject refund(String outTradeNo, String outRefundNo, String refundFee) throws Exception {
        this.logger.info("【建行退费】outTradeNo={},outRefundNo={},refundFee={}", new Object[]{outTradeNo, outRefundNo, refundFee});
        try {
            // 请求序列号
            String REQUEST_SN = UUIDUtils.getRequestSn(16);
            // 商户号
            String CUST_ID = CcbConfig.MERCHANTID;
            // 操作员号
            String USER_ID = CcbConfig.USER_ID;
            // 操作员密码
            String sRequest = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?> \n" +
                    "<TX> \n" +
                    "  <REQUEST_SN>" + REQUEST_SN + "</REQUEST_SN> \n" +
                    "  <CUST_ID>" + CUST_ID + "</CUST_ID> \n" +
                    "  <USER_ID>" + USER_ID + "</USER_ID> \n" +
                    "  <PASSWORD>" + CcbConfig.PASSWORD + "</PASSWORD> \n" +
                    "  <TX_CODE>5W1004</TX_CODE> \n" +
                    "  <LANGUAGE>CN</LANGUAGE> \n" +
                    "  <TX_INFO> \n" +
                    "    <MONEY>" + refundFee +"</MONEY> \n" +
                    "    <ORDER>" + outTradeNo + "</ORDER> \n" +
                    "    <REFUND_CODE></REFUND_CODE> \n" +
                    "  </TX_INFO> \n" +
                    "  <SIGN_INFO></SIGN_INFO> \n" +
                    "  <SIGNCERT></SIGNCERT> \n" +
                    "</TX> \n";
            String refundXml = this.ccbRefund(CcbConfig.ipAddress, CcbConfig.nPort, sRequest);
            JSONObject refundJson = this.parseXml(refundXml);
            if ("000000".equals(refundJson.getString("RETURN_CODE"))){
                // 交易流水信息
                InfoTradeFlow flow = this.infoTradeFlowMapper.queryTradeFlowByOutTradeNo(outTradeNo);
                // 退款流水信息
                InfoRefundFlow refund = new InfoRefundFlow();
                String refundFlowNid = UUIDUtils.getUUID();
                refund.setNid(refundFlowNid);
                refund.setTradeNo(flow.getTradeNo());
                refund.setOutRefundNo(outRefundNo);
                refund.setRefundFee(refundFee);
                refund.setRefundId("ccb");
                refund.setRefundTime(new Date());
                this.infoRefundFlowMapper.insert(refund);
                this.logger.info("【建行退费】成功");
                return ReturnObject.success(null, refundFlowNid);
            } else{
                String errMsg = refundJson.getString("RETURN_MSG");
                if((null == errMsg) || ("".equals(errMsg))){
                    errMsg = "建行退款失败";
                }
                return ReturnObject.error(errMsg);
            }

        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
