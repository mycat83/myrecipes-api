package link.myrecipes.api.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoleSecurity {
    private String authority;

    public String getAuthority() {
        return authority;
    }

    @Builder
    public UserRoleSecurity(String authority) {
        this.authority = authority;
    }
}
