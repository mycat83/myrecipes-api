package link.myrecipes.api.domain;

import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeTag;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "popular_recipe")
@Getter
public class PopularRecipeDocument {

    @Id
    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private Integer people;

    private Integer readCount;

    private List<String> recipeTagList;

    private Integer sequence;

    @Builder
    public PopularRecipeDocument(Integer id, String title, String image, Integer estimatedTime, Integer difficulty,
                                 Integer people, Integer readCount, List<String> recipeTagList) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.people = people;
        this.readCount = readCount;
        this.recipeTagList = recipeTagList;
    }

    public Recipe toDTO() {
        Recipe recipe = Recipe.builder()
                .id(this.getId())
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();

        for (RecipeTag recipeTag: this.getRecipeTagList().stream().map(RecipeTag::new).collect(Collectors.toList())) {
            recipe.addRecipeTag(recipeTag);
        }

        return recipe;
    }
}
