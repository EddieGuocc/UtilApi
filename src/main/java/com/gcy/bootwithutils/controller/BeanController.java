package com.gcy.bootwithutils.controller;


import com.gcy.bootwithutils.common.constants.Result;
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
public class BeanController {

    @Autowired
    BeanService beanService;

    @ApiOperation("返回对象中为null的属性")
    @GetMapping("/filterNullProperty")
    @ResponseBody
    public Result<String []> getNullProperty(@RequestBody(required = true) @Valid Person person){
        House h = new House();
        System.out.println(h.getAddress().equals(""));
        return Result.success(BeanService.getNullPropertyNames(person));
    }

}
