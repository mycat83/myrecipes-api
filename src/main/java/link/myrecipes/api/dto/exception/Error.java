package link.myrecipes.api.dto.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Error implements Serializable {
    private String defaultMessage;
    private String field;

    public Error(String defaultMessage, String field) {
        this.defaultMessage = defaultMessage;
        this.field = field;
    }
}
