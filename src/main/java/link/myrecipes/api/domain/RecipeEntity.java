package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.view.RecipeView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"recipeMaterialEntityList", "recipeStepEntityList", "recipeTagEntityList"})
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer estimatedTime;

    @Column(nullable = false)
    @Max(5)
    @Min(1)
    private Integer difficulty;

    @OneToMany(mappedBy = "recipeEntity", cascade = CascadeType.ALL)
    private List<RecipeMaterialEntity> recipeMaterialEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeEntity", cascade = CascadeType.ALL)
    private List<RecipeStepEntity> recipeStepEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RecipeTagEntity> recipeTagEntityList = new ArrayList<>();

    @Builder
    public RecipeEntity(String title, String image, Integer estimatedTime, Integer difficulty, Integer registerUserId, Integer modifyUserId) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
    }

    public void addRecipeMaterial(RecipeMaterialEntity recipeMaterialEntity) {
        this.recipeMaterialEntityList.add(recipeMaterialEntity);
    }

    public void addRecipeStep(RecipeStepEntity recipeStepEntity) {
        this.recipeStepEntityList.add(recipeStepEntity);
    }

    public void addRecipeTag(RecipeTagEntity recipeTagEntity) {
        this.recipeTagEntityList.add(recipeTagEntity);
    }

    public void clearRecipeMaterialEntityList() {
        this.recipeMaterialEntityList.clear();
    }

    public void clearRecipeStepEntityList() {
        this.recipeStepEntityList.clear();
    }

    public void clearRecipeTagEntityList() {
        this.recipeTagEntityList.clear();
    }

    public void update(int id, RecipeEntity recipeEntity, int userId) {
        this.id = id;
        this.title = recipeEntity.title;
        this.image = recipeEntity.image;
        this.estimatedTime = recipeEntity.estimatedTime;
        this.difficulty = recipeEntity.difficulty;
        this.registerUserId = recipeEntity.registerUserId;
        this.modifyUserId = userId;
    }

    public Recipe toDTO() {
        Recipe recipe = Recipe.builder()
                .id(this.getId())
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();

        for (RecipeTagEntity recipeTagEntity : this.getRecipeTagEntityList()) {
            recipe.addRecipeTag(recipeTagEntity.toDTO());
        }

        return recipe;
    }

    public RecipeView toViewDTO() {
        RecipeView recipeView = RecipeView.builder()
                .id(this.getId() == null ? 0 : this.getId())
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();

        for (RecipeMaterialEntity recipeMaterialEntity : this.getRecipeMaterialEntityList()) {
            recipeView.addRecipeMaterialView(recipeMaterialEntity.toViewDTO());
        }

        for (RecipeStepEntity recipeStepEntity : this.getRecipeStepEntityList()) {
            recipeView.addRecipeStepView(recipeStepEntity.toViewDTO());
        }

        for (RecipeTagEntity recipeTagEntity : this.getRecipeTagEntityList()) {
            recipeView.addRecipeTagView(recipeTagEntity.toViewDTO());
        }

        return recipeView;
    }
}
