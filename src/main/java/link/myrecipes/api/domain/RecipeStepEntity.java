package link.myrecipes.api.domain;

import link.myrecipes.api.dto.view.RecipeStepView;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recipe_step")
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
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
