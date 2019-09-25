package io.myrecipes.api.dto;

import io.myrecipes.api.domain.Recipe;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeDTO {
    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private List<RecipeTagDTO> recipeTagDTOList = new ArrayList<>();

    @Builder
    public RecipeDTO(Integer id, String title, String image, Integer estimatedTime, Integer difficulty) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
    }

    public void addRecipeTagDTO(RecipeTagDTO recipeTagDTO) {
        this.recipeTagDTOList.add(recipeTagDTO);
    }

    public Recipe toDomain() {
        return Recipe.builder()
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();
    }
}
