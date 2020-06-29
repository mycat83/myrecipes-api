package link.myrecipes.api.domain;

import link.myrecipes.api.dto.security.UserRoleSecurity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
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
