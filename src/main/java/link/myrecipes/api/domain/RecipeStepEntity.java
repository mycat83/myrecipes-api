package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import link.myrecipes.api.dto.view.RecipeStepView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_step")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeStepEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer step;

    @Column(nullable = false)
    private String content;

    private String image;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @Builder
    public RecipeStepEntity(Integer step, String content, String image, RecipeEntity recipeEntity) {
        this.step = step;
        this.content = content;
        this.image = image;
        this.recipeEntity = recipeEntity;
    }

    public void setRecipeEntity(RecipeEntity recipeEntity) {
        this.recipeEntity = recipeEntity;
    }

    public RecipeStepView toViewDTO() {
        return RecipeStepView.builder()
                .step(this.getStep())
                .content(this.getContent())
                .image(this.getImage())
                .build();
    }
}
