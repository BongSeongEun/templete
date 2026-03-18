package com.example.templete;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.templete"})
public class TempleteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempleteApplication.class, args);
    }

}
