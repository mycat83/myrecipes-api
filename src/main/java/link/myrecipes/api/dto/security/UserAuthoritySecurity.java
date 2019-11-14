package link.myrecipes.api.dto.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAuthoritySecurity {
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
