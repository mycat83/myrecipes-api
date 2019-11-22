package link.myrecipes.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("link.myrecipes.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(
                        new Tag("system", "시스템 API"),
                        new Tag("baseInfo", "기본정보 API"),
                        new Tag("recipe", "레시피 API"),
                        new Tag("member", "회원 API")
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("마이레시피 API")
                .description("마이레시피 Front/Admin 시스템에서 사용할 Rest API 제공")
                .version("1.0")
                .build();
    }
}
