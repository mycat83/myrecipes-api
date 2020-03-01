package link.myrecipes.api.common;

import link.myrecipes.api.dto.request.UserRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@Component
public class MemberValidator {

    public void validate(UserRequest userRequest, Errors errors) {

        Pattern passwordPattern = Pattern.compile("^.*(?=^.{8,}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$");
        if (!passwordPattern.matcher(userRequest.getPassword()).matches()) {
            errors.rejectValue("password", "wrongValue",
                    "비밀번호는 문자/숫자/특수문자를 조합해서 8자 이상이어야 합니다.");
        }

        Pattern phonePattern = Pattern.compile("^(010|011|016|017|018|019)\\d{7,8}$");
        if (!phonePattern.matcher(userRequest.getPhone()).matches()) {
            errors.rejectValue("phone", "wrongValue",
                    "핸드폰 번호가 올바르지 않습니다.");
        }
    }
}
