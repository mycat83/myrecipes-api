package io.myrecipes.api.dto;

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
}
