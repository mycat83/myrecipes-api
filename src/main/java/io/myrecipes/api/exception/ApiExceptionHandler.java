package io.myrecipes.api.exception;

import io.myrecipes.api.domain.ApiErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorInfo> handleException(Exception ex) {
        log.info(ex.toString());

        ApiErrorInfo apiErrorInfo = new ApiErrorInfo();
        apiErrorInfo.setMessage(ex.toString());

        return new ResponseEntity<>(apiErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
