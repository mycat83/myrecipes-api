package link.myrecipes.api.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class ValidationExceptionInfo {
    private Date timestamp;
    private int status;
    private Error[]errors;

    @Builder
    public ValidationExceptionInfo(Date timestamp, int status, Error[] errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }
}
