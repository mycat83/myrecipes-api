package link.myrecipes.api.exception;

import link.myrecipes.api.dto.exception.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends RuntimeException {
    private final Error[] errors;

    public CustomValidationException(String defaultMessage, String field){
        this.errors = new Error[] {new Error(defaultMessage, field)};
    }

    public CustomValidationException(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }
}
