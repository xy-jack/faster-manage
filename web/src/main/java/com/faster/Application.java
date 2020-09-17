package com.faster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 该项目程序启动入口
 */
@SpringBootApplication
@MapperScan({"com.faster.system.mapper","com.faster.web.mapper", "com.faster.dev.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
