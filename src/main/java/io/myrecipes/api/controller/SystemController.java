package io.myrecipes.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Hello System", HttpStatus.OK);
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public ResponseEntity<String> exception() throws NullPointerException {
        throw new NullPointerException();
    }
}
