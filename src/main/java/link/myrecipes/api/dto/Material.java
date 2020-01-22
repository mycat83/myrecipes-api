package link.myrecipes.api.dto;

import link.myrecipes.api.domain.MaterialEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Material {
    private Integer id;

    private String name;

    private String unitName;

    @Builder
    public Material(Integer id, String name, String unitName) {
        this.id = id;
        this.name = name;
        this.unitName = unitName;
    }

    public MaterialEntity toEntity() {
        return MaterialEntity.builder()
                .name(this.getName())
                .build();
    }
}
