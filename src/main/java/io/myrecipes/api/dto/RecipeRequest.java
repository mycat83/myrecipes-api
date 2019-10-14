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

    private List<RecipeMaterialRequest> recipeMaterialRequestList = new ArrayList<>();

    private List<RecipeStepRequest> recipeStepRequestList = new ArrayList<>();

    private List<RecipeTagRequest> recipeTagRequestList = new ArrayList<>();

    @Builder
    public RecipeRequest(String title, String image, Integer estimatedTime, Integer difficulty) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
    }

    public void addRecipeMaterial(RecipeMaterialRequest recipeMaterialRequest) {
        this.recipeMaterialRequestList.add(recipeMaterialRequest);
    }

    public void addRecipeStep(RecipeStepRequest recipeStepRequest) {
        this.recipeStepRequestList.add(recipeStepRequest);
    }

    public void addRecipeTag(RecipeTagRequest recipeTagRequest) {
        this.recipeTagRequestList.add(recipeTagRequest);
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
