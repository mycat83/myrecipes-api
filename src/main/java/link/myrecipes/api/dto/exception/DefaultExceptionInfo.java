package link.myrecipes.api.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class DefaultExceptionInfo {
    private LocalDateTime timestamp;
    private int status;
    private String message;

    @Builder
    public DefaultExceptionInfo(LocalDateTime timestamp, int status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
