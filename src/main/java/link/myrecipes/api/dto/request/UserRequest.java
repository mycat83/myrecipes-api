package link.myrecipes.api.dto.request;

import link.myrecipes.api.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private String username;

    private String password;

    private String name;

    private String phone;

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
