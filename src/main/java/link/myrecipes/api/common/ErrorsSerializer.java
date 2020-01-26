package link.myrecipes.api.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
@Slf4j
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();
        errors.getFieldErrors().forEach(error ->{
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", error.getObjectName());
                jsonGenerator.writeStringField("defaultMessage", error.getDefaultMessage());
                jsonGenerator.writeStringField("code", error.getCode());
                jsonGenerator.writeStringField("field", error.getField());
                Object rejectedValue = error.getRejectedValue();
                if (rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        });

        errors.getGlobalErrors().forEach(error -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", error.getObjectName());
                jsonGenerator.writeStringField("defaultMessage", error.getDefaultMessage());
                jsonGenerator.writeStringField("code", error.getCode());
                jsonGenerator.writeEndObject();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        });
        jsonGenerator.writeEndArray();
    }
}
