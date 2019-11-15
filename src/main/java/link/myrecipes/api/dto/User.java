package link.myrecipes.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class User {
    private Integer id;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private List<UserAuthority> userAuthorityList = new ArrayList<>();

    @Builder
    public User(Integer id, String username, String password, String name, String phone, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void addUserAuthority(UserAuthority userAuthority) {
        this.userAuthorityList.add(userAuthority);
    }
}
