package com.gcy.bootwithutils.httpclient;


import com.gcy.bootwithutils.common.constants.ErrorMessage;
import com.gcy.bootwithutils.common.constants.ResultCode;
import com.gcy.bootwithutils.service.json.JsonService;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientService {

	@Autowired
    private CloseableHttpClient httpClient;
 
    @Autowired
    private RequestConfig config;

    @Autowired
	private JsonService JsonUtil;
    
    public HttpResult doGet(String url, Map<String, Object> map) throws Exception {   
    	HttpGet get = null;
    	String str = "";
        if (map != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            str = EntityUtils.toString(urlEncodedFormEntity);
            System.out.println(str);
            get = new HttpGet(url+"?"+str);
        }else {
            get = new HttpGet(url);
        }
    	get.setConfig(config);
        // 发起请求
    	CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(get);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
    		
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        return new HttpResult(response.getStatusLine().getStatusCode(), 
        		EntityUtils.toString(response.getEntity(), "UTF-8"));
    }
    
    public HttpResult doGet(String url, Map<String, Object> map,Map<String, Object> headerMap) throws Exception {   
    	HttpGet get = null;
    	String str = "";
        if (map != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            str = EntityUtils.toString(urlEncodedFormEntity);
            System.out.println(str);
            get = new HttpGet(url+"?"+str);
        }else {
            get = new HttpGet(url);
        }
        if (headerMap != null) {
        	for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
        		get.addHeader(entry.getKey(), entry.getValue().toString());
        	}
        }
    	get.setConfig(config);
        // 发起请求
    	CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(get);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
    		
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        return new HttpResult(response.getStatusLine().getStatusCode(), 
        		EntityUtils.toString(response.getEntity(), "UTF-8"));
    }
 
     
    public HttpResult doPostForm(String url) throws Exception {
        return this.doPostForm(url, null,null);
    }
    public HttpResult doPostForm(String url, Map<String, Object> map) throws Exception {
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setConfig(config);
        if (map != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        } 
        // 发起请求
        CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPost);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        return new HttpResult(response.getStatusLine().getStatusCode(), 
        		EntityUtils.toString(response.getEntity(), "UTF-8"));
    }
    
    public HttpResult doPostForm(String url, Map<String, Object> map,Map<String, Object> headerMap) throws Exception {
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setConfig(config);
        if (map != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
        } 
        if (headerMap != null) {
        	for (Map.Entry<String, Object> entry : map.entrySet()) {
        		httpPost.addHeader(entry.getKey(), entry.getValue().toString());
        	}
        }
        // 发起请求
        CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPost);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        return new HttpResult(response.getStatusLine().getStatusCode(), 
        		EntityUtils.toString(response.getEntity(), "UTF-8"));
    }
    
    public HttpResult doPostJson(String url) throws Exception {
        return this.doPostJson(url, null,null);
    }
    
    public HttpResult doPostJson(String url, Map<String, Object> map,Map<String, Object> headerMap) throws Exception {
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setConfig(config);
        if (map != null) {
            httpPost.setEntity(new StringEntity(JsonUtil.toJson(map)));
        } 
        if (headerMap != null) {
        	for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
        		httpPost.addHeader(entry.getKey(), entry.getValue().toString());
        	}
        }
        // 发起请求
        CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPost);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        HttpResult result = null;
        if(response.getStatusLine().getStatusCode() == 204) {
         result = new HttpResult(response.getStatusLine().getStatusCode(), null);
        } else {
         result = new HttpResult(response.getStatusLine().getStatusCode(),
        			EntityUtils.toString(response.getEntity(), "UTF-8"));
        }
        if(response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return result;
    }
    
    public HttpResult doPostJson(String url, Map<String, Object> map) throws Exception {
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setConfig(config);
        if (map != null) {
            httpPost.setEntity(new StringEntity(JsonUtil.toJson(map)));
        } 
        // 发起请求
        CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPost);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        return new HttpResult(response.getStatusLine().getStatusCode(), 
        		EntityUtils.toString(response.getEntity(), "UTF-8"));
    }
    
    public HttpResult doPutJson(String url, Map<String, Object> map,Map<String, Object> headerMap) throws Exception {
    	HttpPut httpPut = new HttpPut(url);
        if (map != null) {
        	httpPut.setEntity(new StringEntity(JsonUtil.toJson(map)));
        } 
        if (headerMap != null) {
        	for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
        		httpPut.addHeader(entry.getKey(), entry.getValue().toString());
        	}
        }
        // 发起请求
        CloseableHttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPut);
		} catch (IOException e1) {
			StringWriter w = new StringWriter();
    		e1.printStackTrace(new PrintWriter(w));
			e1.printStackTrace();
			ErrorMessage em = new ErrorMessage();
			em.setErr_code(ResultCode.UNCONNECTION.getResultCode());
			em.setErr_message(ResultCode.UNCONNECTION.getMessage());
			return new HttpResult(999,JsonUtil.toJson(em));
		}
        HttpResult result = null;
        if(response.getStatusLine().getStatusCode() == 204) {
         result = new HttpResult(response.getStatusLine().getStatusCode(), null);
        } else {
         result = new HttpResult(response.getStatusLine().getStatusCode(),
        			EntityUtils.toString(response.getEntity(), "UTF-8"));
        }
        if(response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return result;
    }
}
