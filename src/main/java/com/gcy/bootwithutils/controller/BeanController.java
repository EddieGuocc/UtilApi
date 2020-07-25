package com.gcy.bootwithutils.controller;


import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.common.exception.Exception401;
import com.gcy.bootwithutils.controller.base.BaseController;
import com.gcy.bootwithutils.service.bean.BeanService;
import com.gcy.bootwithutils.vo.House;
import com.gcy.bootwithutils.vo.Person;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/bean")
public class BeanController extends BaseController {

    @Autowired
    BeanService beanService;

    @ApiOperation("返回对象中为null的属性")
    @GetMapping("/filterNullProperty")
    @ResponseBody
    public Result<String []> getNullProperty(@RequestBody(required = true) @Valid Person person) throws Exception {
        return Result.success(BeanService.getNullPropertyNames(person));
    }

    /*
     *@Method testComplicatedCors
     *@Params [person]
     *@Description 跨域复杂请求实例
     *@Author Eddie
     *@Date 2020/07/25 17:56
     */
    @DeleteMapping("/complicatedCors")
    @ResponseBody
    public Result<String> testComplicatedCors(@RequestBody Person person){
        return Result.success(person.toString());
    }

    /*
     *@Method testSimpleCors
     *@Params [param]
     *@Description 跨域简单请求实例
     *@Author Eddie
     *@Date 2020/07/25 17:56
     */
    @GetMapping("/simpleCors")
    @ResponseBody
    public Result<String> testSimpleCors(@RequestParam("param") String param){
        return Result.success(param);
    }

}
