package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeTagEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeTag {
    private String tag;

    @Builder
    public RecipeTag(String tag) {
        this.tag = tag;
    }

    public RecipeTagEntity toEntity() {
        return RecipeTagEntity.builder()
                .tag(this.getTag())
                .build();
    }
}
