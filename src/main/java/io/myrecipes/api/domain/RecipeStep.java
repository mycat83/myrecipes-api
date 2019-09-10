package io.myrecipes.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipe_step")
public class RecipeStep {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer step;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public RecipeStep(Integer step, String content, String image, Recipe recipe) {
        this.step = step;
        this.content = content;
        this.image = image;
        this.recipe = recipe;
    }
}
