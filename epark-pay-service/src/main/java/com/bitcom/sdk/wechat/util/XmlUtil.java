package com.bitcom.sdk.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;


public class XmlUtil {
    private static Logger log = LoggerFactory.getLogger(XmlUtil.class);

    public static Map<String, String> xmlToEntity(String xmlDoc) {
        Map<String, String> map = new HashMap<String, String>();


        StringReader read = new StringReader(xmlDoc);

        InputSource source = new InputSource(read);

        SAXBuilder sb = new SAXBuilder();

        try {
            Document doc = sb.build(source);

            Element root = doc.getRootElement();

            List<Element> jiedian = root.getChildren();

            Element et = null;
            for (int i = 0; i < jiedian.size(); i++) {
                et = jiedian.get(i);
                map.put(et.getName(), et.getText());
            }

        } catch (JDOMException e) {
            log.error("抛出异常", (Throwable) e);
        } catch (IOException e) {
            log.error("抛出异常", e);
        }
        return map;
    }


    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }


        in.close();

        return m;
    }


    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator<Element> it = children.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }


    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String) doc.getProperty("encoding");
    }

    public static String toXml(Map<String, String> params) {
        StringBuilder buf = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<xml>");
        for (String key : keys) {
            buf.append("<").append(key).append(">");
            buf.append("<![CDATA[").append(params.get(key)).append("]]>");
            buf.append("</").append(key).append(">\n");
        }
        buf.append("</xml>");
        return buf.toString();
    }
}



