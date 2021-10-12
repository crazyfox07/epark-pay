package com.bitcom.sdk.wechat.common;

import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundOrderData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLParser {
    public static List<RefundOrderData> getRefundOrderList(String refundQueryResponseString) throws IOException, SAXException, ParserConfigurationException {
        List<RefundOrderData> list = new ArrayList();

        Map<String, Object> map = getMapFromXML(refundQueryResponseString);

        int count = Integer.parseInt((String) map.get("refund_count"));
        Util.log("count:" + count);

        if (count < 1) {
            return list;
        }


        for (int i = 0; i < count; i++) {
            RefundOrderData refundOrderData = new RefundOrderData();

            refundOrderData.setOutRefundNo(Util.getStringFromMap(map, "out_refund_no_" + i, ""));
            refundOrderData.setRefundID(Util.getStringFromMap(map, "refund_id_" + i, ""));
            refundOrderData.setRefundChannel(Util.getStringFromMap(map, "refund_channel_" + i, ""));
            refundOrderData.setRefundFee(Util.getIntFromMap(map, "refund_fee_" + i));
            refundOrderData.setCouponRefundFee(Util.getIntFromMap(map, "coupon_refund_fee_" + i));
            refundOrderData.setRefundStatus(Util.getStringFromMap(map, "refund_status_" + i, ""));
            list.add(refundOrderData);
        }

        return list;
    }


    public static Map<String, Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = Util.getStringStream(xmlString);
        Document document = builder.parse(is);


        NodeList allNodes = document.getFirstChild().getChildNodes();

        Map<String, Object> map = new HashMap<String, Object>();
        int i = 0;
        while (i < allNodes.getLength()) {
            Node node = allNodes.item(i);
            if (node instanceof org.w3c.dom.Element) {
                map.put(node.getNodeName(), node.getTextContent());
            }
            i++;
        }
        return map;
    }
}



