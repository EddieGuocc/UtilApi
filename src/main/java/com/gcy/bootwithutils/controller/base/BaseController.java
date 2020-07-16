package com.gcy.bootwithutils.controller.base;

import com.gcy.bootwithutils.httpclient.HttpClientService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName BaseController
 * @Description Controller基类
 * @Author Eddie
 * @Date 2020/07/16 14:40
 */
public class BaseController {

    @Autowired
    protected HttpClientService httpclient;
}
