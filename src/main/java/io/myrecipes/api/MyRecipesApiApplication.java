package io.myrecipes.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "io.myrecipes.api.mapper")
public class MyRecipesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyRecipesApiApplication.class, args);
    }

}
