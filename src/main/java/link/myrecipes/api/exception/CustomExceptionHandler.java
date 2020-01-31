package link.myrecipes.api.exception;

import link.myrecipes.api.dto.exception.DefaultExceptionInfo;
import link.myrecipes.api.dto.exception.Error;
import link.myrecipes.api.dto.exception.ValidationExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

//@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus httpStatus =  HttpStatus.BAD_REQUEST;

        Error[] errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new Error(fieldError.getDefaultMessage(), fieldError.getField()))
                .toArray(Error[]::new);

        ValidationExceptionInfo validationExceptionInfo = ValidationExceptionInfo.builder()
                .timestamp(new Date())
                .status(httpStatus.value())
                .errors(errors)
                .build();
        log.error(validationExceptionInfo.toString());

        return new ResponseEntity<>(validationExceptionInfo, httpStatus);
    }

    @ExceptionHandler(NotExistDataException.class)
    public ResponseEntity<DefaultExceptionInfo> handleNotExistDataException(NotExistDataException ex) {
        HttpStatus httpStatus =  HttpStatus.INTERNAL_SERVER_ERROR;

        DefaultExceptionInfo defaultExceptionInfo = DefaultExceptionInfo.builder()
                .timestamp(new Date())
                .status(httpStatus.value())
                .message(ex.toString())
                .build();
        log.error(defaultExceptionInfo.toString());

        return new ResponseEntity<>(defaultExceptionInfo, httpStatus);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<DefaultExceptionInfo> handleUsernameNotFoundException() {
        return new ResponseEntity<>(new DefaultExceptionInfo(), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultExceptionInfo> handleException(Exception ex) {
        HttpStatus httpStatus =  HttpStatus.INTERNAL_SERVER_ERROR;

        DefaultExceptionInfo defaultExceptionInfo = DefaultExceptionInfo.builder()
                .timestamp(new Date())
                .status(httpStatus.value())
                .message(ex.toString())
                .build();
        log.error(defaultExceptionInfo.toString());

        return new ResponseEntity<>(defaultExceptionInfo, httpStatus);
    }
}
