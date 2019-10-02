package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeStepEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeStep {
    private Integer step;

    private String content;

    private String image;

    @Builder
    public RecipeStep(Integer step, String content, String image) {
        this.step = step;
        this.content = content;
        this.image = image;
    }

    public RecipeStepEntity toEntity() {
        return RecipeStepEntity.builder()
                .step(this.getStep())
                .content(this.getContent())
                .image(this.getImage())
                .build();
    }
}
