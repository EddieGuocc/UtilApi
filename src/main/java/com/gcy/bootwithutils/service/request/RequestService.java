package com.gcy.bootwithutils.service.request;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

@Service
@Slf4j
public class RequestService {


    /*
     * @Author gcy
     * @Description get request info
     * @Date 17:41 2020/5/18
     * @Param [request]
     * @return java.lang.String
     **/
    public static String getBasePath(HttpServletRequest request){
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme).append("://").append(domain);
        if("http".equalsIgnoreCase(scheme) && 80 != port){
            basePath.append(":").append(String.valueOf(port));
        }else if("https".equalsIgnoreCase(scheme) && 443 != port){
            basePath.append(":").append(String.valueOf(port));
        }
        return basePath.toString();

    }

    /*
     * @Author gcy
     * @Description get IP address
     * @Date 17:43 2020/5/18
     * @Param [request]
     * @return java.lang.String
     **/
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
