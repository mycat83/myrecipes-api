package io.myrecipes.api.dto;

import io.myrecipes.api.domain.Recipe;
import lombok.Builder;

public class RecipeTagDTO {
    private String tag;

    private Recipe recipe;

    @Builder
    public RecipeTagDTO(String tag) {
        this.tag = tag;
    }
}
