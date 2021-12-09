package com.gcy.bootwithutils.utils;

import com.gcy.bootwithutils.common.constants.Brower;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * request 对象的相关操作
 */
public class RequestUtil {

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            if (request.getCharacterEncoding().toUpperCase().equals("ISO-8859-1")) {
                valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            }
            //判空及字符串null
            if (StringUtils.isBlank(valueStr) || "null".equalsIgnoreCase(valueStr)) {
                valueStr = "";
            }
            params.put(name, StringUtils.isNotBlank(valueStr) ? valueStr.trim() : valueStr);
        }
        return params;
    }

    /**
     * 获取客户端ip
     *
     * @param request
     * @return ip 字符串
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String realIp = ip;
        if (StringUtils.isNotBlank(ip) && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            realIp = ipArray[0].trim();
        }
        return realIp.trim();
    }

    /**
     * 获取本地IP地址
     * @return ip 字符串
     */
    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取请求的完整url
     *
     * @param request
     * @return
     */
    public static String getUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String query = getQueryStringDecode(request);
        if (query != null)
            url.append("?").append(query);
        return url.toString();

    }

    /**
     * 获取请求的referer
     *
     * @param request
     * @return
     */
    public static String getRefererUrl(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    public static String getQueryStringDecode(HttpServletRequest request) {

        if (request.getMethod().equals("GET")) {// 判断是否是get请求方式，不是get请求则直接返回
            String queryString = request.getQueryString();
            Map<String, String> mapDecoder = getMapFromQueryStringDecoder(queryString, "utf-8");
            queryString = getQueryStringFromMap(mapDecoder);
            return queryString;
        } else {
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder queryString = new StringBuilder();
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (String s : values) {
                    String value = null;
                    try {
                        value = URLDecoder.decode(s, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //屏蔽敏感字段
                    if (key.equals("idcard") || key.equals("cvv2") || key.equals("validdate") || key.equals("cardno")) {
                        value = "******";
                    }
                    queryString.append(key).append("=").append(value).append("&");
                }
            }
            if (queryString.length() > 0) {
                return queryString.substring(0, queryString.length() - 1);
            } else {
                return queryString.toString();
            }
        }
    }

    public static Map<String, String> getMapFromQueryStringDecoder(String queryString, String charset) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (queryString == null) {
            return null;
        }
        String[] paramArr = queryString.split("&");
        for (String tmp : paramArr) {
            String[] field = tmp.split("=");
            if (field.length != 2) {
                continue;
            }
            String value = null;
            try {
                value = URLDecoder.decode(field[1], charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            paramMap.put(field[0], value);
        }
        return paramMap;
    }

    public static String getQueryStringFromMap(Map<String, String> queryMap) {
        if (null == queryMap || queryMap.isEmpty()) {
            return null;
        }
        Object[] objArr = queryMap.keySet().toArray();
        // Arrays.sort(objArr);

        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (Object key : objArr) {
            buf.append(i == 0 ? "" : "&").append(key).append("=").append(queryMap.get(key));
            i++;
        }
        return buf.toString();
    }

    /**
     * 获取整个url
     * @param baseUrl 基本的url（不带参数）
     * @param params url参数
     * @return
     */
    public static String getFullUrl(String baseUrl, Map<String, String> params) {
        StringBuilder fullUrl = new StringBuilder(baseUrl);
        if (params == null) {
            return baseUrl;
        }
        Set<String> keys = params.keySet();
        if (keys.size() == 0) {
            return baseUrl;
        }
        Iterator<String> it = keys.iterator();
        int index = 0;
        while (it.hasNext()) {
            String key = it.next();
            if (index == 0) {
                fullUrl.append("?").append(key).append("=").append(params.get(key));
            } else {
                fullUrl.append("&").append(key).append("=").append(params.get(key));
            }
            index++;
        }

        return fullUrl.toString();
    }

    public static String getHostAndContext(HttpServletRequest request){
        String url = request.getScheme()+"://"; // 请求协议 http 或 https
        url+=request.getHeader("host");// 请求服务器
        url+=request.getContextPath();//工程名
        return url;
    }

    public static String getSortQueryStringFromMap(Map<String, String> paramMap) {
        if (null == paramMap || paramMap.isEmpty()) {
            return null;
        }
        Object[] objArr = paramMap.keySet().toArray();
        Arrays.sort(objArr);
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (Object obj : objArr) {
            String tmp = obj.toString();
            if (tmp.startsWith("d_") || tmp.equals("sign")) {
                continue;
            }
            buf.append(i == 0 ? "" : "&").append(tmp).append("=")
                    .append(paramMap.get(tmp));
            i++;
        }
        return buf.toString();
    }

    public static Brower getBrower(final HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if( userAgent != null && userAgent.toLowerCase().contains(Brower.WXBrower.getAgentKeyWord())){
            return Brower.WXBrower;
        }else if( userAgent != null && userAgent.toLowerCase().contains(Brower.ALIAPP.getAgentKeyWord())) {
            return Brower.ALIAPP;
        }else {
            return Brower.OTHER;
        }
    }

    /**
     * 返回request中指定参数为空的集合
     * 判断requset中指定的参数是否为空
     * @param request
     * @param keyStr 逗号分割,非空参数名
     * @return
     */
    public static List<String> blankParam(HttpServletRequest request, String keyStr) {
        List<String> blankParam = Lists.newArrayList();
        if(StringUtils.isBlank(keyStr)) {
            return blankParam;
        }
        Map<String, String> reqParam = RequestUtil.getParameterMap(request);
        String[] keys = keyStr.split(",");
        List<String> keyList = Arrays.asList(keys);
        for (String key:keyList) {
            key = key.trim();
            if(!reqParam.containsKey(key)) {
                blankParam.add(key);
                continue;
            }
            if(reqParam.get(key) == null) {
                blankParam.add(key);
                continue;
            }
            if(reqParam.get(key) instanceof String) {
                String value = String.valueOf(reqParam.get(key));
                if(StringUtils.isBlank(value)) {
                    blankParam.add(key);
                }
            }
        }
        return blankParam;
    }

    /**
     * 读取request流
     *
     * @param request
     * @return
     */
    public static String readReqStr(HttpServletRequest request) throws IOException {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(request
                    .getInputStream(), StandardCharsets.UTF_8));
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (null != reader)
            {
                reader.close();
            }
        }

        return sb.toString();
    }
}
