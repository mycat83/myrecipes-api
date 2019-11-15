package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserAuthorityEntity {
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
    public UserAuthorityEntity(String authority) {
        this.authority = authority;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
