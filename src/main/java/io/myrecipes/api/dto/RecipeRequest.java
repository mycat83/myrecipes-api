package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequest {
    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private List<RecipeMaterial> recipeMaterialList = new ArrayList<>();

    private List<RecipeStep> recipeStepList = new ArrayList<>();

    private List<RecipeTag> recipeTagList = new ArrayList<>();

    @Builder
    public RecipeRequest(String title, String image, Integer estimatedTime, Integer difficulty) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
    }

    public void addRecipeMaterial(RecipeMaterial recipeMaterial) {
        this.recipeMaterialList.add(recipeMaterial);
    }

    public void addRecipeStep(RecipeStep recipeStep) {
        this.recipeStepList.add(recipeStep);
    }

    public void addRecipeTag(RecipeTag recipeTag) {
        this.recipeTagList.add(recipeTag);
    }

    public RecipeEntity toEntity() {
        return RecipeEntity.builder()
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();
    }
}
