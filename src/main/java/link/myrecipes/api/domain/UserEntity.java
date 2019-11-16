package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.security.UserSecurity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "userAuthorityEntityList")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean accountNonExpired;

    @Column(nullable = false)
    private Boolean accountNonLocked;

    @Column(nullable = false)
    private Boolean credentialsNonExpired;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<UserAuthorityEntity> userAuthorityEntityList = new ArrayList<>();

    @Builder
    public UserEntity(String username, String password, String name, String phone, String email, Boolean accountNonExpired, Boolean accountNonLocked,
                      Boolean credentialsNonExpired, Boolean enabled, Integer registerUserId, Integer modifyUserId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
    }

    public void addUserAuthority(UserAuthorityEntity userAuthorityEntity) {
        this.userAuthorityEntityList.add(userAuthorityEntity);
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void update(UserEntity userEntity, int userId) {
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.name = userEntity.getName();
        this.phone = userEntity.getPhone();
        this.email = userEntity.getEmail();
        this.modifyUserId = userId;
    }

    public UserSecurity toSecurityDTO() {
        return UserSecurity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .accountNonExpired(this.getAccountNonExpired())
                .accountNonLocked(this.getAccountNonLocked())
                .credentialsNonExpired(this.getCredentialsNonExpired())
                .enabled(this.getEnabled())
                .build();
    }

    public User toDTO() {
        return User.builder()
                .id(this.getId())
                .username(this.getUsername())
                .password(this.getPassword())
                .name(this.getName())
                .phone(this.getPhone())
                .email(this.getEmail())
                .build();
    }
}
