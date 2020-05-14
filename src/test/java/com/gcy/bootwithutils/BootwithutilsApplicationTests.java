package com.gcy.bootwithutils;

import com.gcy.bootwithutils.service.bean.BeanService;
import com.gcy.bootwithutils.service.date.DateService;
import com.gcy.bootwithutils.vo.House;
import com.gcy.bootwithutils.vo.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BootwithutilsApplicationTests {


    @Test
    public void contextLoads() throws Exception {
        System.out.println(DateService.stampToDate("1590940800000"));
    }



}
