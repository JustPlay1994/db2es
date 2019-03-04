package com.justplay1994.github.db2es.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * @Package: com.justplay1994.github.db2es.client
 * @Project: db2es
 * @Creator: huangzezhou
 * @Create_Date: 2018/11/29 19:54
 * @Updater: huangzezhou
 * @Update_Date: 2018/11/29 19:54
 * @Update_Description: huangzezhou 补充
 * @Description: //TODO
 **/

/**
 * RequestConfig的默认配置:
 this.staleConnectionCheckEnabled = false;
 this.redirectsEnabled = true;
 this.maxRedirects = 50;
 this.relativeRedirectsAllowed = true;
 this.authenticationEnabled = true;
 this.connectionRequestTimeout = -1;
 this.connectTimeout = -1;
 this.socketTimeout = -1;
 this.contentCompressionEnabled = true;

 setDefaultMaxPerRoute = 2
 */
public class HttpClientUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilTest.class);

    static PoolingHttpClientConnectionManager cm;
    static CloseableHttpClient httpClient;
    static {

        cm = new PoolingHttpClientConnectionManager();

        //将目标主机的最大连接数增加到50
//        HttpHost localhost = new HttpHost("10.192.19.161", 9200);
//        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        // 总的最大连接数
        cm.setMaxTotal(200);
        // 每个路由的最大连接数，不能超过总的最大连接数
        cm.setDefaultMaxPerRoute(200);


        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    public static String get(String url) throws IOException, URISyntaxException {
        String result = "";
        URIBuilder builder = new URIBuilder(url);

        HttpGet httpGet = new HttpGet(builder.build());
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(6000)
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000).build();
        httpGet.setConfig(config);
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        HttpResponse response = httpClient.execute(httpGet);
        StatusLine status = response.getStatusLine();                   //获取返回的状态码
        HttpEntity entity = response.getEntity();                       //获取响应内容
        result = EntityUtils.toString(entity, "UTF-8");
        if (! (status.getStatusCode() == HttpStatus.SC_OK) ) {
            logger.error("get request error:\n"+result);
        }
        httpGet.abort();//中止请求，连接被释放回连接池
        return result;
    }

    @Test
    public void test() throws IOException, URISyntaxException {
        for (int i = 0; i < 1000; ++i) {
            get("http://www.baidu.com");
        }
    }

}