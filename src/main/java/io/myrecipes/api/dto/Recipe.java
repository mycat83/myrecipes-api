package io.myrecipes.api.dto;

import io.myrecipes.api.domain.RecipeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {
    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    // TODO: 마지막 페이지 여부 추가

    private List<RecipeTag> recipeTagList = new ArrayList<>();

    @Builder
    public Recipe(Integer id, String title, String image, Integer estimatedTime, Integer difficulty) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
    }

    public void addRecipeTagDTO(RecipeTag recipeTag) {
        this.recipeTagList.add(recipeTag);
    }

    public RecipeEntity toDomain() {
        return RecipeEntity.builder()
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();
    }
}
