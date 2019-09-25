package io.myrecipes.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeTag {
    private String tag;

    @Builder
    public RecipeTag(String tag) {
        this.tag = tag;
    }
}
