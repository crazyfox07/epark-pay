package com.bitcom.sdk.wechat.common;

import com.bitcom.sdk.wechat.service.IServiceRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;


public class HttpsRequest
        implements IServiceRequest {
    private static Log log = new Log(LoggerFactory.getLogger(HttpsRequest.class));


    private boolean hasInit = false;


    private int socketTimeout = 10000;


    private int connectTimeout = 30000;


    private RequestConfig requestConfig;


    private CloseableHttpClient httpClient;


    public HttpsRequest() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        init();
    }


    private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(Configure.getCertLocalPath()));
        try {
            keyStore.load(instream, Configure.getCertPassword().toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }


        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Configure.getCertPassword().toCharArray()).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);


        this.httpClient = HttpClients.custom().setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();


        this.requestConfig = RequestConfig.custom().setSocketTimeout(this.socketTimeout).setConnectTimeout(this.connectTimeout).build();

        this.hasInit = true;
    }


    public String sendPost(String url, Object xmlObj) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        if (!this.hasInit) {
            init();
        }

        String result = null;

        HttpPost httpPost = new HttpPost(url);


        XStream xStreamForRequestPostData = new XStream((HierarchicalStreamDriver) new DomDriver("UTF-8", (NameCoder) new XmlFriendlyNameCoder("-_", "_")));


        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);

        Util.log("API，POST过去的数据是：");
        Util.log(postDataXML);


        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity((HttpEntity) postEntity);


        httpPost.setConfig(this.requestConfig);

        Util.log("executing request" + httpPost.getRequestLine());

        try {
            CloseableHttpResponse closeableHttpResponse = this.httpClient.execute((HttpUriRequest) httpPost);

            HttpEntity entity = closeableHttpResponse.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            log.e("http get throw ConnectionPoolTimeoutException(wait time out)");
        } catch (ConnectTimeoutException e) {
            log.e("http get throw ConnectTimeoutException");
        } catch (SocketTimeoutException e) {
            log.e("http get throw SocketTimeoutException");
        } catch (Exception e) {
            log.e("http get throw Exception");
        } finally {

            httpPost.abort();
        }

        return result;
    }


    public void setSocketTimeout(int socketTimeout) {
        socketTimeout = socketTimeout;
        resetRequestConfig();
    }


    public void setConnectTimeout(int connectTimeout) {
        connectTimeout = connectTimeout;
        resetRequestConfig();
    }


    private void resetRequestConfig() {
        this.requestConfig = RequestConfig.custom().setSocketTimeout(this.socketTimeout).setConnectTimeout(this.connectTimeout).build();
    }


    public void setRequestConfig(RequestConfig requestConfig) {
        requestConfig = requestConfig;
    }

    public static interface ResultListener {
        void onConnectionPoolTimeoutError();
    }
}



