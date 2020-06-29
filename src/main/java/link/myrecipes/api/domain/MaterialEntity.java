package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties("hibernateLazyInitializer")
public class MaterialEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_name")
    private UnitEntity unitEntity;

    @Builder
    public MaterialEntity(String name, Integer registerUserId, Integer modifyUserId, UnitEntity unitEntity) {
        this.name = name;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
        this.unitEntity = unitEntity;
    }
}
