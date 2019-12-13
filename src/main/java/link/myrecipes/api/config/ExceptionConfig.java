package link.myrecipes.api.config;

import link.myrecipes.api.exception.CustomValidationException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class ExceptionConfig {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
                Throwable error = getError(webRequest);
                if (error instanceof CustomValidationException) {
                    errorAttributes.put("errors", ((CustomValidationException) error).getErrors());
                }
                return errorAttributes;
            }
        };
    }
}
