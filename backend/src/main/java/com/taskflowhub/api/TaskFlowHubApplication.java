package com.taskflowhub.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.taskflowhub.api.mapper")
public class TaskFlowHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskFlowHubApplication.class, args);
    }
}
