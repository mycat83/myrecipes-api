package link.myrecipes.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAuthority {
    private String authority;

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
