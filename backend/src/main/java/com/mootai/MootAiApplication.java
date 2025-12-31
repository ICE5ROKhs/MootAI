package com.mootai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Slf4j
public class MootAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MootAiApplication.class, args);
    }

    /**
     * 应用启动成功后显示提示信息
     */
    @Bean
    @Order(2)
    public ApplicationRunner applicationReadyRunner() {
        return args -> {
            log.info("[SUCCESS] MootAI Backend Service Started!");
        };
    }
}

