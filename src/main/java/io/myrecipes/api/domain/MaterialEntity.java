package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.myrecipes.api.dto.Material;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class MaterialEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_name")
    private UnitEntity unitEntity;

    @Builder
    public MaterialEntity(String name, Integer registerUserId, Integer modifyUserId, UnitEntity unitEntity) {
        this.name = name;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
        this.unitEntity = unitEntity;
    }
    
    public void setUnitEntity(UnitEntity unitEntity) {
        this.unitEntity = unitEntity;
    }

    public Material toDTO() {
        return Material.builder()
                .id(this.getId())
                .name(this.getName())
                .unitName(this.getUnitEntity().getName())
                .build();
    }
}
