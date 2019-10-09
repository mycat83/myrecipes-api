package io.myrecipes.api.dto;

import io.myrecipes.api.domain.MaterialEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
