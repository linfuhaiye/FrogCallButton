package com.guohuasoft.frogagv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.guohuasoft"})
@MapperScan("com.guohuasoft.**.mappers")
public class FrogAgvApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrogAgvApplication.class, args);
    }
}
