package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Material extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_name")
    private Unit unit;

    @Builder
    public Material(String name, Integer registerUserId, Integer modifyUserId, Unit unit) {
        this.name = name;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
        this.unit = unit;
    }
}
