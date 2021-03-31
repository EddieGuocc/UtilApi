package com.gcy.bootwithutils;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.gcy"})
@MapperScan("com.gcy.bootwithutils.dao")
public class BootwithutilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootwithutilsApplication.class, args);
    }

}
