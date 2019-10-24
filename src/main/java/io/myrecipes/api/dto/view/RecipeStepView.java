package io.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeStepView {
    private Integer step;

    private String content;

    private String image;

    @Builder
    public RecipeStepView(Integer step, String content, String image) {
        this.step = step;
        this.content = content;
        this.image = image;
    }
}
