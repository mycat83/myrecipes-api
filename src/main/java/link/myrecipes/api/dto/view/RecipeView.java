package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeView implements Serializable {
    private Integer id;

    private String title;

    private String image;

    private Integer estimatedTime;

    private Integer difficulty;

    private Integer registerUserId;

    private LocalDateTime registerDate;

    private List<RecipeMaterialView> recipeMaterialViewList = new ArrayList<>();

    private List<RecipeStepView> recipeStepViewList = new ArrayList<>();

    private List<RecipeTagView> recipeTagViewList = new ArrayList<>();

    @Builder
    public RecipeView(Integer id, String title, String image, Integer estimatedTime, Integer difficulty,
                      Integer registerUserId, LocalDateTime registerDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.registerUserId = registerUserId;
        this.registerDate = registerDate;
    }

    public void addRecipeMaterialView(RecipeMaterialView recipeMaterialView) {
        this.recipeMaterialViewList.add(recipeMaterialView);
    }

    public void addRecipeStepView(RecipeStepView recipeStepView) {
        this.recipeStepViewList.add(recipeStepView);
    }

    public void addRecipeTagView(RecipeTagView recipeTagView) {
        this.recipeTagViewList.add(recipeTagView);
    }
}
