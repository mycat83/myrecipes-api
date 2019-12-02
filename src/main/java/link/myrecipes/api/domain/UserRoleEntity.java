package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import link.myrecipes.api.dto.security.UserRoleSecurity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserRoleEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String authority;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder
    public UserRoleEntity(String authority) {
        this.authority = authority;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserRoleSecurity toSecurityDTO() {
        return UserRoleSecurity.builder()
                .authority(this.getAuthority())
                .build();
    }
}
