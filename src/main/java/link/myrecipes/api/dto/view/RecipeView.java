package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeView implements Serializable {

    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private Integer people;

    private Integer registerUserId;

    private LocalDateTime registerDate;

    private List<RecipeMaterialView> recipeMaterialList = new ArrayList<>();

    private List<RecipeStepView> recipeStepList = new ArrayList<>();

    private List<RecipeTagView> recipeTagList = new ArrayList<>();

    @Builder
    public RecipeView(Integer id, String title, String image, Integer estimatedTime, Integer difficulty,
                      Integer people, Integer registerUserId, LocalDateTime registerDate) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.people = people;
        this.registerUserId = registerUserId;
        this.registerDate = registerDate;
    }

    public void addRecipeMaterial(RecipeMaterialView recipeMaterialView) {
        this.recipeMaterialList.add(recipeMaterialView);
    }

    public void addRecipeStep(RecipeStepView recipeStepView) {
        this.recipeStepList.add(recipeStepView);
    }

    public void addRecipeTag(RecipeTagView recipeTagView) {
        this.recipeTagList.add(recipeTagView);
    }
}
