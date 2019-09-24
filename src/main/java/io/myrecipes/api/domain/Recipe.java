package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.myrecipes.api.dto.RecipeDTO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"recipeMaterialList", "recipeStepList", "recipeTagList"})
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Recipe extends BaseEntity {
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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeMaterial> recipeMaterialList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeStep> recipeStepList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RecipeTag> recipeTagList = new ArrayList<>();

    @Builder
    public Recipe(String title, String image, Integer estimatedTime, Integer difficulty, Integer registerUserId, Integer modifyUserId) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
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

    public void update (Recipe recipe) {
        this.title = recipe.title;
        this.image = recipe.image;
        this.estimatedTime = recipe.estimatedTime;
        this.difficulty = recipe.difficulty;
        this.registerUserId = recipe.registerUserId;
        this.modifyUserId = recipe.modifyUserId;
    }

    public <T> T toDTO() {
        ModelMapper modelMapper = new ModelMapper();
        Type type = new TypeToken<T>() {}.getType();
        return modelMapper.map(this, type);
    }
}
