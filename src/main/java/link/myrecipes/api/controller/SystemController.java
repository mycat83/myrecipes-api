package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.domain.RecipeEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"system"})
@RestController
public class SystemController {
    @GetMapping(value = "/health")
    @ApiOperation("API 상태")
    public ResponseEntity<RecipeEntity> health() {
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .title("test1")
                .image("test1.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .registerUserId(1001)
                .build();
        return new ResponseEntity<>(recipeEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/exception")
    @ApiOperation("API 예외 발생")
    public ResponseEntity<String> exception() throws NullPointerException {
        throw new NullPointerException();
    }
}
