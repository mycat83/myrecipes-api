package link.myrecipes.api.exception;

import link.myrecipes.api.dto.exception.DefaultExceptionInfo;
import link.myrecipes.api.dto.exception.ValidationError;
import link.myrecipes.api.dto.exception.ValidationExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus httpStatus =  HttpStatus.BAD_REQUEST;

        List<ValidationError> validationErrorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getDefaultMessage(), fieldError.getField()))
                .collect(Collectors.toList());

        ValidationExceptionInfo validationExceptionInfo = ValidationExceptionInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .validationErrorList(validationErrorList)
                .build();
        log.error(validationExceptionInfo.toString());

        return new ResponseEntity<>(validationExceptionInfo, httpStatus);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ValidationExceptionInfo> handleCustomValidationException(CustomValidationException ex) {
        HttpStatus httpStatus =  HttpStatus.BAD_REQUEST;

        List<ValidationError> validationErrorList = Collections.singletonList(
                new ValidationError(ex.getErrors()[0].getDefaultMessage(), ex.getErrors()[0].getField())
        );

        ValidationExceptionInfo validationExceptionInfo = ValidationExceptionInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .validationErrorList(validationErrorList)
                .build();
        log.error(validationExceptionInfo.toString());

        return new ResponseEntity<>(validationExceptionInfo, httpStatus);
    }

    @ExceptionHandler(NotExistDataException.class)
    public ResponseEntity<DefaultExceptionInfo> handleNotExistDataException(NotExistDataException ex) {
        HttpStatus httpStatus =  HttpStatus.INTERNAL_SERVER_ERROR;

        DefaultExceptionInfo defaultExceptionInfo = DefaultExceptionInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .message(ex.getMessage())
                .build();
        log.error(defaultExceptionInfo.toString());

        return new ResponseEntity<>(defaultExceptionInfo, httpStatus);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<DefaultExceptionInfo> handleUsernameNotFoundException() {
        return new ResponseEntity<>(new DefaultExceptionInfo(), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultExceptionInfo> handleException(DefaultExceptionInfo ex) {
        HttpStatus httpStatus =  HttpStatus.INTERNAL_SERVER_ERROR;

        DefaultExceptionInfo defaultExceptionInfo = DefaultExceptionInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(defaultExceptionInfo, httpStatus);
    }
}
