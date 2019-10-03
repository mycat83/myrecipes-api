package io.myrecipes.api.dto;

import io.myrecipes.api.domain.MaterialEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Material {
    private String name;

    private String unitName;

    @Builder
    public Material(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    public MaterialEntity toEntity() {
        return MaterialEntity.builder()
                .name(this.getName())
                .build();
    }
}
