package com.bitcom.sdk.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsRequest {
    private Logger logger = LoggerFactory.getLogger(getClass());


    private boolean hasInit = false;

    private int socketTimeout = 10000;


    private int connectTimeout = 30000;


    private RequestConfig requestConfig;


    private CloseableHttpClient httpClient;


    public HttpsRequest(String certLocalPath, String password) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        init(certLocalPath, password);
    }


    private void init(String certLocalPath, String password) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream inStream = new FileInputStream(new File(certLocalPath));
        try {
            keyStore.load(inStream, password.toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            inStream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();


        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, (HostnameVerifier) new DefaultHostnameVerifier());
        this.httpClient = HttpClients.custom().setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();

        this.requestConfig = RequestConfig.custom().setSocketTimeout(this.socketTimeout).setConnectTimeout(this.connectTimeout).build();
        this.hasInit = true;
    }


    public String sendPost(String url, Map<String, String> xmlObj) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        String postDataXML = XmlUtil.toXml(xmlObj);

        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity((HttpEntity) postEntity);

        httpPost.setConfig(this.requestConfig);
        try {
            CloseableHttpResponse closeableHttpResponse = this.httpClient.execute((HttpUriRequest) httpPost);
            HttpEntity entity = closeableHttpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            this.logger.error(e.getMessage());
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
}



