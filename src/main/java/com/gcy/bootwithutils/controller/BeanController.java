package com.gcy.bootwithutils.controller;


import com.gcy.bootwithutils.common.annotation.RedisLock;
import com.gcy.bootwithutils.common.constants.RedisLockTypeEnum;
import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.common.constants.ResultCode;
import com.gcy.bootwithutils.common.exception.Exception400;
import com.gcy.bootwithutils.controller.base.BaseController;
import com.gcy.bootwithutils.model.vo.StudentVo;
import com.gcy.bootwithutils.service.bean.BeanService;
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
    public Result<String []> getNullProperty(@RequestBody(required = true) @Valid StudentVo student) throws Exception {
        return Result.success(BeanService.getNullPropertyNames(student));
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
    public Result<String> testComplicatedCors(@RequestBody StudentVo person){
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

    @GetMapping("/lock")
    @RedisLock(typeEnum = RedisLockTypeEnum.BUSINESS_A, lockTime = 12)
    public Result<String> get(@RequestParam("id") String id) {
        System.out.println("controller in " + id);
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            throw new Exception400(ResultCode.UNCONNECTION);
        }
        return Result.success("success");
    }

}
