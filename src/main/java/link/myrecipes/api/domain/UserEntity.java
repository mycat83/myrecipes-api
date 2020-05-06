package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "userRoleEntityList")
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
    private List<UserRoleEntity> userRoleEntityList = new ArrayList<>();

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

    public void addUserRole(UserRoleEntity userRoleEntity) {
        this.userRoleEntityList.add(userRoleEntity);
    }

    public void update(UserEntity userEntity, int userId) {
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.name = userEntity.getName();
        this.phone = userEntity.getPhone();
        this.email = userEntity.getEmail();
        this.modifyUserId = userId;
    }
}
