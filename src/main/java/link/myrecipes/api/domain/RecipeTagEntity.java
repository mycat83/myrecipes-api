package link.myrecipes.api.domain;

import link.myrecipes.api.dto.RecipeTag;
import link.myrecipes.api.dto.view.RecipeTagView;
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
@Table(name = "recipe_tag")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeTagEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
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
