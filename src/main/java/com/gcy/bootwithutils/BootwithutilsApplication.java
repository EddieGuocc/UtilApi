package com.gcy.bootwithutils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BootwithutilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootwithutilsApplication.class, args);
    }

}
