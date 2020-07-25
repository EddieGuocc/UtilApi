package com.gcy.bootwithutils.common.configuration.interceptor;

import com.gcy.bootwithutils.common.interceptor.OauthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName InterceptorConfiguration
 * @Description TODO
 * @Author Eddie
 * @Date 2020/07/23 10:16
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    OauthInterceptor oauthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(oauthInterceptor).addPathPatterns("/**").excludePathPatterns("/appleAuth/**");
    }

    /*
     *@Method addCorsMappings
     *@Params [registry]
     *@Description
     *       addMapping 配置跨域路径
     *       allowCredentials 允许所有的请求方法处理cookie
     *       allowedMethods 允许所有的请求方法访问该跨域资源服务器
     *       allowedOrigins 允许所有的请求域名访问我们的跨域资源
     *       allowedHeaders 允许所有的请求header访问
     * 关于跨域 CORS(Cross-Origin Resource Sharing)
     * 跨域分为两种请求
     *      1. 简单请求
     *          请求方法为 GET POST HEAD
     *          Content-Type为 application/x-www-form-urlencoded、multipart/form-data、text/plain
     *      2. 复杂请求
     *           非简单请求都是复杂请求
     *
     * 当没有配置跨域的时候（以下代码注释掉）
     * 简单请求，访问后，会添加一个origin字段，程序将此字段与配置文件进行匹配，匹配成功即可跨域访问并在 HTTP信息头中添加
     * Access-Control-Allow-Origin，否则，报错
     * [No 'Access-Control-Allow-Origin' header is present on the requested resource.]
     * 复杂请求，访问时，会首先发出预检请求（OPTION）,将请求的真实信息（方法、自定义头、origin信息等）添加到 HTTP头中,
     * 询问服务器是否可以访问。
     *       请求参数  【Access-Control-Request-Method】 【Access-Control-Request-Headers】 【Origin】
     * 收到 OPTION 请求后服务器会对字段进行验证 然后相应头会携带以下参数
     *      返回参数    【Access-Control-Allow-Credentials】 是否允许用户发送、处理 cookie
     *                【Access-Control-Allow-Headers】 服务器允许使用的头字段
     *                【Access-Control-Allow-Methods】 真实请求允许的方法
     *                【Access-Control-Max-Age】 预检请求的有效期，单位为秒。有效期内，不会重复发送预检请求
     * 预检请求通过后，将会发送真实请求
     *
     *@Author Eddie
     *@Date 2020/07/23 11:09
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOrigins("*").allowedMethods("*");
    }
}
