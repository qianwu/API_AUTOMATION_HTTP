package com.api.wq.protocol.http.domain;

import com.alibaba.fastjson.JSON;
import com.api.wq.common.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import org.apache.commons.collections.MapUtils;


public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static HttpConfig globalHttpConfig;
    private static Long SSOCookieExpireTime = 3 * 60000L;
    private static int connectTimeout = 60000;
    private static int socketTimeout = 60000;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static Map<String, Map<String, String>> sidMap = new HashMap<>();
    private static ExpiryMap<String, Map<String, Object>> bucCookieMap = new ExpiryMap<>();
    private HttpConfig.HttpMode currentHttpMode = null;
    private Map<String, String[]> currentHostList = null;

    private HttpConfig customHttpConfig = new HttpConfig();

    public HttpUtil() {
        customHttpConfig.setLogSwitch(false);
    }

    public HttpUtil(HttpConfig config) {
        customHttpConfig.setLogSwitch(config.getLogSwitch());
    }

    public HttpUtil(HttpConfig config, Map<String, String[]> hostList) {
        customHttpConfig.setLogSwitch(config.getLogSwitch());
        currentHttpMode = HttpConfig.HttpMode.DNS;
        this.currentHostList = hostList;
    }

    public Map<String, String[]> getCurrentHostList() {
        return currentHostList;
    }

    public void setCurrentHostList(Map<String, String[]> currentHostList) {
        currentHostList = currentHostList;
    }

    public com.api.wq.protocol.http.domain.HttpConfig.HttpMode getCurrentHttpMode() {
        return currentHttpMode;
    }

    public void setCurrentHttpMode(HttpConfig.HttpMode currentHttpMode) {
        currentHttpMode = currentHttpMode;
    }

    static {
        loadConfig();
    }

    public HttpResponseBase get(String url, Map<String, Object> headers, Map<String, Object> params) {
        HttpGet get = new HttpGet();
        CloseableHttpResponse response = null;
        String returnContent = null;
        HttpEntity entity = null;
        HttpResponseBase httpResponseBase = new HttpResponseBase();
        HttpClientContext context = HttpClientContext.create();

        try {
            globalHttpConfig.setCurrentUrl(url);
            CloseableHttpClient httpClient = getHttpClient(globalHttpConfig);
            List<NameValuePair> paramsPairs = new ArrayList<>();
            for(Map.Entry<String,Object> entry:params.entrySet()){
                NameValuePair pair = new BasicNameValuePair(entry.getKey(),getDataString(entry.getValue()));
                paramsPairs.add(pair);
            }
            get = new HttpGet(url);
            get.setConfig(globalHttpConfig.getRequestConfig());
            String str = EntityUtils.toString(new UrlEncodedFormEntity(paramsPairs,"UTF-8"));
            URI uri = StringUtil.isBlank(str) ? new URI(get.getURI().toString()) : new URI(
                    get.getURI().toString() + "?" + str);
            get.setURI(uri);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, Object> header : headers.entrySet()) {
                    get.setHeader(header.getKey(), getDataString(header.getValue()));
                }
            }
            response = doExecute(httpClient, get, context);

            HttpRequestContext httpRequestContext = buildHttpRequestContext(url, headers, params, null);
            httpResponseBase = buildHttpResponseBase(response, context, httpRequestContext, null);
            get.releaseConnection();
            response.close();
            httpClient.close();
            return httpResponseBase;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.debug(get.getURI().toString());
            logger.debug(returnContent);
        }
        return httpResponseBase;
    }

    public HttpResponseBase get(String url,Map<String,Object> headers,String params){
        HttpGet get = new HttpGet();
        CloseableHttpResponse response = null;
        String returnContent = null;
        HttpEntity entity = null;
        HttpResponseBase httpResponseBase = new HttpResponseBase();
        HttpClientContext context = HttpClientContext.create();
        try{
            globalHttpConfig.setCurrentUrl(url);
            CloseableHttpClient httpClient = getHttpClient(globalHttpConfig);
            get = new HttpGet(url);
            get.setConfig(globalHttpConfig.getRequestConfig());
            URI uri = StringUtil.isBlank(params) ? new URI(get.getURI().toString()) :new URI(get.getURI().toASCIIString() +"?"+params);
            get.setURI(uri);
            if(MapUtils.isNotEmpty(headers)){
                for(Map.Entry<String,Object> header:headers.entrySet()){
                    get.setHeader(header.getKey(),getDataString(header.getValue()));
                }
            }
            response = doExecute(httpClient,get,context);
            HttpRequestContext httpRequestContext = buildHttpRequestContext(url,headers,null,params);
            httpResponseBase = buildHttpResponseBase(response,context,httpRequestContext,null);
            get.releaseConnection();
            response.close();
            httpClient.close();
            return httpResponseBase;
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseBase;
    }

    public HttpResponseBase get(String url) {
        return get(url, new HashMap<>(), new HashMap<>());
    }


    public HttpResponseBase post(String url,Map<String,Object> headers,Map<String,Object> params){
        HttpPost post = new HttpPost();
        String returnContent = null;
        HttpResponseBase httpResponseBase = new HttpResponseBase();
        HttpClientContext context = HttpClientContext.create();
        try {
            globalHttpConfig.setCurrentUrl(url);
            CloseableHttpClient httpClient = getHttpClient(globalHttpConfig);
            post = new HttpPost(url);
            post.setConfig(globalHttpConfig.getRequestConfig());
            List<NameValuePair> paramsPairs = new ArrayList<>();
            for(Map.Entry<String,Object> entry:params.entrySet()){
                NameValuePair pair = new BasicNameValuePair(entry.getKey(),getDataString(entry.getValue()));
                paramsPairs.add(pair);
            }
            post.setEntity(new StringEntity(String.valueOf(paramsPairs),"UTF-8"));
            if(MapUtils.isNotEmpty(headers)){
                for(Map.Entry<String,Object> header:headers.entrySet()){
                    post.setHeader(header.getKey(),getDataString(header.getValue()));
                }
            }
            CloseableHttpResponse response = doExecute(httpClient,post,context);
            HttpRequestContext httpRequestContext = buildHttpRequestContext(url,headers,params,null);
            httpResponseBase = buildHttpResponseBase(response,context,httpRequestContext,null);
            post.releaseConnection();
            response.close();
            httpClient.close();
            return httpResponseBase;
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseBase;
    }



    public HttpResponseBase post(String url,Map<String,Object> headers,String body){
        HttpPost post = new HttpPost();
        String returnContent = null;
        HttpResponseBase httpResponseBase = new HttpResponseBase();
        HttpClientContext context = HttpClientContext.create();
        try {
            globalHttpConfig.setCurrentUrl(url);
            CloseableHttpClient httpClient = getHttpClient(globalHttpConfig);
            post = new HttpPost(url);
            post.setConfig(globalHttpConfig.getRequestConfig());
            post.setEntity(new StringEntity(body,"UTF-8"));
            if(MapUtils.isNotEmpty(headers)){
                for(Map.Entry<String,Object> header:headers.entrySet()){
                    post.setHeader(header.getKey(),getDataString(header.getValue()));
                }
            }
            CloseableHttpResponse response = doExecute(httpClient,post,context);
            HttpRequestContext httpRequestContext = buildHttpRequestContext(url,headers,null,body);
            httpResponseBase = buildHttpResponseBase(response,context,httpRequestContext,null);
            post.releaseConnection();
            response.close();
            httpClient.close();
            return httpResponseBase;
        }catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseBase;
    }

    public HttpResponseBase post(String url) {
        return post(url, new HashMap<>(), "");
    }

    public CloseableHttpClient getHttpClient(HttpConfig httpConfig) {
        HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        BasicHttpClientConnectionManager connectionManager = null;
        CloseableHttpClient httpClient = null;
        RequestConfig requestConfig = null;
        if (null != currentHttpMode) {
            httpConfig.setHttpMode(currentHttpMode);
        }
        if (null != currentHostList) {
            httpConfig.setHostList(currentHostList);
        }
        switch (httpConfig.getHttpMode()) {
            case DNS:
                if (null != httpConfig.getCurrentUrl() && isMatchDNSConfig(httpConfig)) {
                    connectionManager = new BasicHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                                    .register(HTTPS, getSslsf())
                                    .build());
                    requestConfig = RequestConfig.custom()
                            .setConnectTimeout(connectTimeout)
                            .setSocketTimeout(socketTimeout)
                            .build();
                } else {
                    connectionManager = new BasicHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                                    .register(HTTPS, getSslsf())
                                    .build(), null, null, new MyDnsResolver(httpConfig));
                    requestConfig = RequestConfig.custom()
                            .setConnectTimeout(connectTimeout)
                            .setSocketTimeout(socketTimeout)
                            .build();
                }
                break;
            case PROXY:
                Boolean isConnected = testProxyPort(httpConfig.getHttpProxyIp(), httpConfig.getHttpProxyPort());
                if (isConnected){
                    connectionManager = new BasicHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                                    .register(HTTPS, getSslsf())
                                    .build());
                    HttpHost httpHost = new HttpHost(httpConfig.getHttpProxyIp(), httpConfig.getHttpProxyPort());
                    requestConfig = RequestConfig.custom()
                            .setConnectTimeout(connectTimeout)
                            .setSocketTimeout(socketTimeout)
                            .setProxy(httpHost)
                            .build();
                }
                else {
                    connectionManager = new BasicHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                                    .register(HTTPS, getSslsf())
                                    .build());
                    requestConfig = RequestConfig.custom()
                            .setConnectTimeout(connectTimeout)
                            .setSocketTimeout(socketTimeout)
                            .build();
                }
                break;
            case DEFAULT:
            default:
                connectionManager = new BasicHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                                .register(HTTPS, getSslsf())
                                .build());
                requestConfig = RequestConfig.custom()
                        .setConnectTimeout(connectTimeout)
                        .setSocketTimeout(socketTimeout)
                        .build();
                break;
        }
        httpConfig.setRequestConfig(requestConfig);

        httpClient = HttpClients.custom().setRetryHandler(httpRequestRetryHandler)
                .setConnectionManager(connectionManager).build();
        //CaseRecordImp.getInstance().log(LogLevelEnum.INFO, ContentTypeEnum.TXT, "调试日志:httpmode:"+JSON.toJSONString
        // (httpConfig.getHttpMode()));
        return httpClient;
    }

    private Boolean isMatchDNSConfig(HttpConfig httpConfig) {
        String currentUrl = httpConfig.getCurrentUrl();
        Map<String, String[]> hostList = httpConfig.getHostList();
        Iterator hostIt = hostList.entrySet().iterator();
        while (hostIt.hasNext()) {
            Map.Entry<String, String[]> entry = (Map.Entry)hostIt.next();
            for (int i = 0; i < entry.getValue().length; i++) {
                if (currentUrl.contains(entry.getValue()[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void loadConfig() {
        globalHttpConfig = new HttpConfig();

        Properties httpConfigProp = new Properties();
        Properties dnsProp = new Properties();

        HttpConfig.HttpMode httpMode = HttpConfig.HttpMode.DEFAULT;
        Map<String, String[]> hostList = null;

        String httpProxyIp = null;
        int httpProxyPort = 0;

        //读取httConfig配置
        InputStream httpConfigInput = Object.class.getResourceAsStream("/httpclient.properties");
        //如果为normal，不处理
        try {
            httpConfigProp.load(httpConfigInput);
            httpMode = HttpConfig.HttpMode.valueOf(httpConfigProp.getProperty("http.mode"));
        } catch (Exception e) {
            logger.debug("请忽略，httpConfig不存在或者httpMode配置错误，走默认配置");
        }
        switch (httpMode) {
            case DNS:
                //dns，读取dns配置
                InputStream dnsInput = Object.class.getResourceAsStream("/dns.properties");
                try {
                    dnsProp.load(dnsInput);
                    hostList = getHostList(dnsProp);
                } catch (Exception e) {
                    logger.debug("请忽略，dns配置读取错误，走默认配置");
                }
                break;
            case PROXY:
                //proxy，读取proxy配置
                try {
                    httpProxyIp = httpConfigProp.getProperty("http.proxy.ip");
                    httpProxyPort = Integer.valueOf(httpConfigProp.getProperty("http.proxy.port"));
                    httpMode = HttpConfig.HttpMode.PROXY;
                } catch (Exception e) {
                    logger.debug("请忽略，proxy配置读取错误，走默认配置");
                }
                break;
            case DEFAULT:
            default:
                httpMode = HttpConfig.HttpMode.DEFAULT;
                break;
        }
        try {
            connectTimeout = Integer.valueOf(httpConfigProp.getProperty("http.connect.timeout"));
            socketTimeout = Integer.valueOf(httpConfigProp.getProperty("http.socket.timeout"));
        } catch (Exception e) {
            logger.debug("请忽略，超时配置读取错误，走默认配置");
        }

        globalHttpConfig.setHttpMode(httpMode);
        globalHttpConfig.setHttpProxyIp(httpProxyIp);
        globalHttpConfig.setHttpProxyPort(httpProxyPort);
        globalHttpConfig.setHostList(hostList);
    }


    public static SSLConnectionSocketFactory getSslsf() {
        SSLConnectionSocketFactory sslsf = null;
        SSLContextBuilder builder = null;
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(),
                    new String[] {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    public static Map<String, String[]> getHostList(Properties properties) throws Exception {
        Map<String, String[]> hostList = new HashMap<>();
        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            logger.debug(key.toString() + "=" + properties.get(key));
            String ip = key.toString();
            String domains = properties.get(key).toString();
            String[] domainList = domains.split("\\s+");
            hostList.put(ip, domainList);
        }
        return hostList;
    }

    private static class MyDnsResolver implements DnsResolver {
        private Map<String, InetAddress[]> mappings = new HashMap();

        public MyDnsResolver(HttpConfig httpConfig) {
            this.addResolve(httpConfig.getHostList());
        }

        private void addResolve(String host, String ip) {
            try {
                this.mappings.put(host, new InetAddress[] {InetAddress.getByName(ip)});
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        private void addResolve(Map<String, String[]> hostList) {
            try {
                Iterator hostIt = hostList.entrySet().iterator();
                while (hostIt.hasNext()) {
                    Map.Entry<String, String[]> entry = (Map.Entry)hostIt.next();
                    for (int i = 0; i < entry.getValue().length; i++) {
                        addResolve(entry.getValue()[i], entry.getKey());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            return this.mappings.containsKey(host) ? (InetAddress[])this.mappings.get(host) : new InetAddress[0];
        }
    }


    private static Boolean testProxyPort(String ip,Integer port){
        Boolean connected = false;
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            connected = socket.isConnected();
            socket.close();
        } catch (IOException e) {
            System.err.println("httpclient.properties中开启了代理模式，但是本地没有启动代理服务，请启动代理服务或者修改配置，已自动降级成普通模式");
        }
        return connected;
    }


    public static String getDataString(Object o) {
        return o instanceof String ? (String)o : JSON.toJSONString(o);
    }

    private CloseableHttpResponse doExecute(CloseableHttpClient httpClient, HttpRequestBase request,
                                            HttpClientContext context) {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request, context);
        } catch (Exception e) {
            httpClient = getDefaultHttpClient();
            try {
                response = httpClient.execute(request, context);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return response;
    }

    private CloseableHttpClient getDefaultHttpClient() {
        HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        BasicHttpClientConnectionManager connectionManager = null;
        CloseableHttpClient httpClient = null;
        connectionManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                        .register(HTTPS, getSslsf())
                        .build());
        httpClient = HttpClients.custom().setRetryHandler(httpRequestRetryHandler)
                .setConnectionManager(connectionManager).build();
        return httpClient;
    }

    public HttpRequestContext buildHttpRequestContext(String url, Map<String, Object> headers,
                                                      Map<String, Object> params, String body) throws Exception {
        HttpRequestContext httpRequestContext = new HttpRequestContext();
        httpRequestContext.setUrl(url);
        httpRequestContext.setHeaders(headers);
        httpRequestContext.setParams(params);
        httpRequestContext.setBody(body);

        return httpRequestContext;
    }

    public HttpResponseBase buildHttpResponseBase(HttpResponse httpResponse, HttpClientContext context,
                                                  HttpRequestContext httpRequestContext, byte[] outputStream)
            throws Exception {

        String returnContent = null;

        HttpResponseBase httpResponseBase = new HttpResponseBase();
        HttpEntity entity = httpResponse.getEntity();
        if (null == outputStream) {
            returnContent = EntityUtils.toString(entity, "UTF-8");
        }
        int responseCode = httpResponse.getStatusLine().getStatusCode();
        Map<String, String> header = handleHttpHeaders(httpResponse.getAllHeaders());
        httpResponseBase.setCode(responseCode);
        httpResponseBase.setHeaders(header);
        httpResponseBase.setResponse(returnContent);
        httpResponseBase.setCookies(context.getCookieStore());
        httpResponseBase.setHttpRequestContext(httpRequestContext);
        httpResponseBase.setOutputStream(outputStream);

//        if (this.customHttpConfig.getLogSwitch()) {
//            //接口调用打印，debug级别的log只会在失败的时候记录到testreport
//            String apiMsg = "调用接口：" + httpRequestContext.getUrl();
//            CaseRecordImp.getInstance().logOperate(LogLevelEnum.INFO, ContentTypeEnum.TXT, apiMsg);
//            String headerMsg = "接口请求header：" + JSON.toJSONString(httpRequestContext.getHeaders());
//            CaseRecordImp.getInstance().log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, headerMsg);
//            String parameterMsg = "接口请求参数：" + JSON.toJSONString(httpRequestContext.getParams());
//            CaseRecordImp.getInstance().log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, parameterMsg);
//            String bodyMsg = "接口请求body：" + JSON.toJSONString(httpRequestContext.getBody());
//            CaseRecordImp.getInstance().log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, bodyMsg);
//            String respText = "接口响应文本：" + returnContent;
//            CaseRecordImp.getInstance().log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, respText);
//            String respHeaderMsg = "接口响应header：" + JSON.toJSONString(header);
//            CaseRecordImp.getInstance().log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, respHeaderMsg);
//        }
        return httpResponseBase;
    }

    public static Map<String, String> handleHttpHeaders(org.apache.http.Header[] headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            String headerName = headers[i].getName();
            String headerValue = headers[i].getValue();
            headerMap.put(headerName, headerValue);
        }
        return headerMap;
    }

}
