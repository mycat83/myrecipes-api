package link.myrecipes.api.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoleSecurity {
    private String role;

    public String getRole() {
        return role;
    }

    @Builder
    public UserRoleSecurity(String role) {
        this.role = role;
    }
}
