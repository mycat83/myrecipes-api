package link.myrecipes.api.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ValidationExceptionInfo {
    private LocalDateTime timestamp;
    private int status;
    private List<ValidationError> validationErrorList;

    @Builder
    public ValidationExceptionInfo(LocalDateTime timestamp, int status, List<ValidationError> validationErrorList) {
        this.timestamp = timestamp;
        this.status = status;
        this.validationErrorList = validationErrorList;
    }
}
