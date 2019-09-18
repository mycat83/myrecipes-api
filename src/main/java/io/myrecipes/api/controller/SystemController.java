package io.myrecipes.api.controller;

import io.myrecipes.api.domain.Recipe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"system"})
@RestController
public class SystemController {
    @GetMapping(value = "/health")
    @ApiOperation("API 상태")
    public ResponseEntity<Recipe> health() {
        Recipe recipe = new Recipe("test1", "test1.jpg", 30, "1", 1001);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping(value = "/exception")
    @ApiOperation("API 예외 발생")
    public ResponseEntity<String> exception() throws NullPointerException {
        throw new NullPointerException();
    }
}
