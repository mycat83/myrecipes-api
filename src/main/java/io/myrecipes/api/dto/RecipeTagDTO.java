package io.myrecipes.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeTagDTO {
    private String tag;

    @Builder
    public RecipeTagDTO(String tag) {
        this.tag = tag;
    }
}
