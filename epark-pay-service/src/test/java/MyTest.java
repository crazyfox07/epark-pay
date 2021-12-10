import com.alibaba.fastjson.JSONObject;
import com.bitcom.config.CcbConfig;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jdom.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MyTest2 {
    private static  final String MERCHANTID = CcbConfig.MERCHANTID;

    public void printStr(){
        System.out.println(this.MERCHANTID);
    }
}

public class MyTest {



    public static String ccbRefund(String ipAdress, int nPort, String sRequest){
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
            e.printStackTrace();
        } finally {
            try{
                if(in != null){
                    in.close();
                }
                if(out != null){
                    out.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return sResult;
    }

    public static void testCcbRefund(){
        String ipAddress = "127.0.0.1";
        int nPort = 12345;
        String sRequest = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?> \n" +
                "<TX> \n" +
                "  <REQUEST_SN>20211207987457</REQUEST_SN> \n" +
                "  <CUST_ID>105000075235505</CUST_ID> \n" +
                "  <USER_ID>lxw</USER_ID> \n" +
                "  <PASSWORD>lanneng2020</PASSWORD> \n" +
                "  <TX_CODE>5W1004</TX_CODE> \n" +
                "  <LANGUAGE>CN</LANGUAGE> \n" +
                "  <TX_INFO> \n" +
                "    <MONEY>0.01</MONEY> \n" +
                "    <ORDER>202112071619204953</ORDER> \n" +
                "    <REFUND_CODE></REFUND_CODE> \n" +
                "  </TX_INFO> \n" +
                "  <SIGN_INFO></SIGN_INFO> \n" +
                "  <SIGNCERT></SIGNCERT> \n" +
                "</TX> \n";
        String result = ccbRefund(ipAddress, nPort, sRequest);
        System.out.println(result);
    };

    public static void parseXml(String xmlStr){
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
            System.out.println(resultJson.toJSONString());
            System.out.println(resultJson.get("hello"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void generateRandomNum(){
        int randomNum;
        String resultStr = "";
        for(int i = 0; i < 16; i++){
            randomNum = (int)(Math.random() * 10);
            resultStr = resultStr + randomNum;
            System.out.println(randomNum);
        }
        System.out.println(resultStr);
    }
    public static void main(String[] args) {
//        String xmlStr =  "<?xml version=\"1.0\" encoding=\"GB18030\"?><TX>  <REQUEST_SN>20211207987457</REQUEST_SN>  <CUST_ID>105000075235505</CUST_ID>  <TX_CODE>5W1004</TX_CODE>  <RETURN_CODE>000000</RETURN_CODE>  <RETURN_MSG></RETURN_MSG>  <LANGUAGE>CN</LANGUAGE>  <TX_INFO>    <ORDER_NUM>202112071619204953</ORDER_NUM>    <PAY_AMOUNT>0.01</PAY_AMOUNT>    <AMOUNT>0.01</AMOUNT>    <REM1></REM1>    <REM2></REM2>      </TX_INFO></TX>";
//        parseXml(xmlStr);
//        MyTest2 myTest2 = new MyTest2();
//        myTest2.printStr();
        generateRandomNum();
    }
}
