package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import link.myrecipes.api.dto.RecipeTag;
import link.myrecipes.api.dto.view.RecipeTagView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeTagEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String tag;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @Builder
    public RecipeTagEntity(String tag, RecipeEntity recipeEntity) {
        this.tag = tag;
        this.recipeEntity = recipeEntity;
    }

    public void setRecipeEntity(RecipeEntity recipeEntity) {
        this.recipeEntity = recipeEntity;
    }

    RecipeTag toDTO() {
        return RecipeTag.builder()
                .tag(this.getTag())
                .build();
    }

    RecipeTagView toViewDTO() {
        return RecipeTagView.builder()
                .tag(this.getTag())
                .build();
    }
}
