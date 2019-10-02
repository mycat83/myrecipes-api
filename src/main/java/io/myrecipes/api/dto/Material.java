package io.myrecipes.api.dto;

import io.myrecipes.api.domain.MaterialEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Material {
    private String name;

    private Integer registerUserId;

    private Integer modifyUserId;

    private String unitName;

    @Builder
    public Material(String name, Integer registerUserId, Integer modifyUserId, String unitName) {
        this.name = name;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
        this.unitName = unitName;
    }

    public MaterialEntity toEntity() {
        return MaterialEntity.builder()
                .name(this.getName())
                .registerUserId(this.getRegisterUserId())
                .modifyUserId(this.getModifyUserId())
                .build();
    }
}
