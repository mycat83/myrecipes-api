package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeReq {
    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private List<RecipeMaterial> recipeMaterialList = new ArrayList<>();

    private List<RecipeStep> recipeStepList = new ArrayList<>();

    private List<RecipeTag> recipeTagList = new ArrayList<>();

    public RecipeEntity toEntity() {
        return RecipeEntity.builder()
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();
    }
}
