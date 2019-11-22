package link.myrecipes.api.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAuthoritySecurity {
    private String authority;

    public String getAuthority() {
        return authority;
    }

    @Builder
    public UserAuthoritySecurity(String authority) {
        this.authority = authority;
    }
}
