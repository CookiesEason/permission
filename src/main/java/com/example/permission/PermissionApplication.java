package com.example.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.example.permission.dao")
public class PermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

}

