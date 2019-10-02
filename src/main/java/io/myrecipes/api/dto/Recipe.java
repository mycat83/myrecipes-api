package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Recipe {
    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private Integer registerUserId;

    private Integer modifyUserId;

    private List<RecipeTag> recipeTagList = new ArrayList<>();

    @Builder
    public Recipe(Integer id, String title, String image, Integer estimatedTime, Integer difficulty, Integer registerUserId, Integer modifyUserId) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
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
                .registerUserId(this.getRegisterUserId())
                .modifyUserId(this.getModifyUserId())
                .build();
    }
}
