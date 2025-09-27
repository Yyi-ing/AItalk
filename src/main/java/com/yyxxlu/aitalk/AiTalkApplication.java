package com.yyxxlu.aitalk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;


@MapperScan("com.yyxxlu.aitalk.mapper")
@SpringBootApplication
public class AiTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTalkApplication.class, args);
    }

}
