package link.myrecipes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyRecipesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyRecipesApiApplication.class, args);
    }

}
