package link.myrecipes.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiErrorInfo {
    private String message;

    @Builder
    public ApiErrorInfo(String message) {
        this.message = message;
    }
}
