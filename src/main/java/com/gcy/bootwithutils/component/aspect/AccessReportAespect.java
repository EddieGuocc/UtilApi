package com.gcy.bootwithutils.component.aspect;


import com.gcy.bootwithutils.service.date.DateService;
import com.gcy.bootwithutils.service.file.FileService;
import com.gcy.bootwithutils.service.json.JsonService;
import com.gcy.bootwithutils.service.request.RequestService;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * @Author gcy
 * @Description 记录对RESTFul接口的访问记录
 * @Date 16:26 2020/5/18
 * @Param
 * @return
 **/

//regard this class as a pointcut component
@Component
@Aspect
@Order(1)
@Slf4j
public class AccessReportAespect {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /*
     * @Author gcy
     * @Description define a pointcut method(void method)
     * @Date 11:48 2020/5/19
     * @Param []
     * @return void
     * [public] can be written or not
     * first [*] express return type can be any type
     * second [*] express all classes under [com.gcy.bootwithutils.controller] package
     * third [*] express all methods in specified class
     * [..] in brackets express numbers and type of params can be any
     **/
    @Pointcut("execution(public * com.gcy.bootwithutils.controller.*.*(..))")
    public void accessReport(){

    }

    @Before("accessReport()")
    public void deBefore(JoinPoint joinPoint){
        //System.out.println("executing before the pointcut method");
        startTime.set(System.currentTimeMillis());
    }

    @After("accessReport()")
    public void doAfter(JoinPoint joinPoint){
        //System.out.println("executing after the pointcut method");
    }

    /*
     * @Author gcy
     * @Description executing during the pointcut method
     * @Date 12:11 2020/5/19
     * @Param [joinPoint]
     * @return java.lang.Object
     **/
    @Around("accessReport()")
    public Object adAround(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        AccessReport accessReport = new AccessReport();
        Object result = joinPoint.proceed();

        //获取切点方法签名对象 Result com.gcy.bootwithutils.controller.BeanController.getNullProperty(Person)
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        //方法上的注释如果有ApiOperation 则获取它的值
        if(method.isAnnotationPresent(ApiOperation.class)){
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            accessReport.setDescription(log.value());
        }
        //设置操作用户名 accessReport.setUsername()
        //获取所在类对象 signature.getDeclaringType().toString()
        //获取声明类型名 signature.getDeclaringTypeName()
        //连接点类型
        //连接点类型"+joinPoint.getKind()
        //连接点静态部分"+staticPart.toLongString()

        accessReport.setBasePath(RequestService.getBasePath(request));
        accessReport.setIp(RequestService.getIpAddr(request));
        accessReport.setMethod(request.getMethod());
        accessReport.setParameter(getParameters(method, joinPoint.getArgs()));
        accessReport.setResult(result);
        accessReport.setSpendTime((int) (System.currentTimeMillis() - startTime.get()));
        accessReport.setStartTime(startTime.get());
        accessReport.setUri(request.getRequestURI());
        accessReport.setUrl(request.getRequestURL().toString());

        log.info("Access report --> {}", JsonService.objectToJson(accessReport));
        return result;
    }



    /*
     * @Author gcy
     * @Description get exception info with AOP and write info into log
     * @Date 17:59 2020/5/19
     * @Param [joinPoint, exception]
     * @return void
     **/
    @AfterThrowing(pointcut = "accessReport()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception){
        if(exception instanceof Exception){
            String nowTime = DateService.currentFormattedMilliseconds("yyyy-MM-dd");
            FileService.generateErrorLog(exception.getClass().toString(),
                    exception.getStackTrace()[0].toString(),nowTime );
            log.error("==========["+nowTime+"] ERROR, For more details in ErrorLog Folder==========");
        }
    }


    /*
     * @Author gcy
     * @Description get request parameters using method and args
     * @Date 18:20 2020/5/18
     * @Param [method, args]
     * @return java.lang.Object
     **/
    private Object getParameters(Method method, Object[] args){
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for(int i = 0; i < parameters.length; i++){
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null){
                argList.add(args[i]);
            }
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null){
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if(!StringUtils.isEmpty(requestParam.value())){
                    key = requestParam.value();
                }

                if (args[i] instanceof MultipartFile){
                    map.put(key, ((MultipartFile)args[i]).getOriginalFilename());
                }else{
                    map.put(key, args[i]);
                }
                argList.add(map);
            }
        }
        if (argList.size() == 0){
            return null;
        }else if(argList.size() == 1){
            return argList.get(0);
        }
        return argList;
    }

    /*
     * @Author gcy
     * @Description pointcut return class
     * @Date 13:52 2020/5/19
     * @Param
     * @return
     **/

    @Getter
    @Setter
    public static class AccessReport {

        /** 操作描述 */
        private String description;

        /** 操作用户 */
        private String username;

        /** 操作时间 */
        private Long startTime;

        /** 消耗时间 */
        private Integer spendTime;

        /** 根路径 */
        private String basePath;

        /** URI */
        private String uri;

        /** URL */
        private String url;

        /** 请求类型 */
        private String method;

        /** IP地址 */
        private String ip;

        /** 请求参数 */
        private Object parameter;

        /** 返回结果 */
        private Object result;
    }

}
