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
@Table(name = "recipe_tag")
public class RecipeTag {
    @Id
    @GeneratedValue
    private Integer id;

    private String tag;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public RecipeTag(String tag, Recipe recipe) {
        this.tag = tag;
        this.recipe = recipe;
    }
}
