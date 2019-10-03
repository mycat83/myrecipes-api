package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeMaterialEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeMaterial {
    private Integer materialId;

    private Integer quantity;

    @Builder
    public RecipeMaterial(Integer materialId, Integer quantity) {
        this.materialId = materialId;
        this.quantity = quantity;
    }

    public RecipeMaterialEntity toEntity() {
        return RecipeMaterialEntity.builder()
                .quantity(this.getQuantity())
                .build();
    }
}
