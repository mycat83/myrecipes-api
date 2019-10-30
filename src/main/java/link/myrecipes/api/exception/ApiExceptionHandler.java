package link.myrecipes.api.exception;

import link.myrecipes.api.dto.ApiErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(NotExistDataException.class)
    public ResponseEntity<ApiErrorInfo> handleNotExistDataException(Exception ex) {
        log.info(ex.toString());

        ApiErrorInfo apiErrorInfo = ApiErrorInfo.builder()
                .message(ex.toString())
                .build();

        return new ResponseEntity<>(apiErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorInfo> handleException(Exception ex) {
        log.info(ex.toString());

        ApiErrorInfo apiErrorInfo = ApiErrorInfo.builder()
                .message(ex.toString())
                .build();

        return new ResponseEntity<>(apiErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
