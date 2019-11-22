package link.myrecipes.api.dto.request;

import link.myrecipes.api.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min = 6, max = 20, message = "아이디는 6~20자 이내로 입력이 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(max = 100, message = "비밀번호는 최대 100자까지 입력이 가능합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(max = 20, message = "이름은 최대 20자까지 입력이 가능합니다.")
    private String name;

    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    @Length(max = 11, message = "핸드폰 번호는 최대 11자까지 입력이 가능합니다.")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Length(max = 40, message = "이메일은 최대 40자까지 입력이 가능합니다.")
    @Email(message = "이메일을 올바른 형식으로 입력해주세요.")
    private String email;

    @Builder
    public UserRequest(String username, String password, String name, String phone, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .name(this.getName())
                .phone(this.getPhone())
                .email(this.getEmail())
                .build();
    }
}
