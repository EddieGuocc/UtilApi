package com.gcy.bootwithutils.httpclient;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HttpClientConfig {
	
	@Value("${httpclient.maxTotal}")
	private Integer maxTotal;
	 
	@Value("${httpclient.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;
	 
	@Value("${httpclient.connectTimeout}")
	private Integer connectTimeout;
	 
	@Value("${httpclient.connectionRequestTimeout}")
	private Integer connectionRequestTimeout;
	 
	@Value("${httpclient.socketTimeout}")
	private Integer socketTimeout;
	 
	 
	/**
     * 首先实例化一个连接池管理器，设置最大连接数、并发连接数
     * @return
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }
 
    /**
     * 实例化连接池，设置连接池管理器。
     * 这里需要以参数形式注入上面实例化的连接池管理器
     * @param httpClientConnectionManager
     * @return
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){
 
        //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
 
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        httpClientBuilder.setRetryHandler(new HttpRequestRetryHandler() {
	        public boolean retryRequest(
	                IOException exception,
	                int executionCount,
	                HttpContext context) {
	            if (executionCount >= 3) {
	                // Do not retry if over max retry count
	                return false;
	            }
//	            if (exception instanceof InterruptedIOException) {
//	                // Timeout
//	                return false;
//	            }
//	            if (exception instanceof UnknownHostException) {
//	                // Unknown host
//	                return false;
//	            }
//	            if (exception instanceof ConnectTimeoutException) {
//	                // Connection refused
//	                return false;
//	            }
//	            if (exception instanceof SSLException) {
//	                // SSL handshake exception
//	                return false;
//	            }
//	            HttpClientContext clientContext = HttpClientContext.adapt(context);
//	            HttpRequest request = clientContext.getRequest();
//	            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
//	            if (idempotent) {
//	                // Retry if the request is considered idempotent
//	                return true;
//	            }
	            return true;
	        }
	
	    });
        return httpClientBuilder;
    }
 
    /**
     * 注入连接池，用于获取httpClient
     * @param httpClientBuilder
     * @return
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }
 
    /**
     * Builder是RequestConfig的一个内部类
     * 通过RequestConfig的custom方法来获取到一个Builder对象
     * 设置builder的连接信息
     * 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
     * @return
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }
 
    /**
     * 使用builder构建一个RequestConfig对象
     * @param builder
     * @return
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
        return builder.build();
    }
}
