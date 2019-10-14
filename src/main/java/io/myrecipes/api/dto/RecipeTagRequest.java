package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeTagEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeTagRequest {
    private String tag;

    @Builder
    public RecipeTagRequest(String tag) {
        this.tag = tag;
    }

    public RecipeTagEntity toEntity() {
        return RecipeTagEntity.builder()
                .tag(this.getTag())
                .build();
    }
}
