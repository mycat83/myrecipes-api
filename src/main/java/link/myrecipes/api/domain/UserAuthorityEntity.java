package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UserAuthorityEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String authority;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
