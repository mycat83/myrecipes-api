package io.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"system"})
@RestController
public class SystemController {
    @ApiOperation("API 상태")
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Hello System", HttpStatus.OK);
    }

    @ApiOperation("API 예외 발생")
    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public ResponseEntity<String> exception() throws NullPointerException {
        throw new NullPointerException();
    }
}
