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
    private String role;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder
    public UserRoleEntity(String role) {
        this.role = role;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserRoleSecurity toSecurityDTO() {
        return UserRoleSecurity.builder()
                .role(this.getRole())
                .build();
    }
}
