package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_step")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeStep {
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
    private Recipe recipe;

    @Builder
    public RecipeStep(Integer step, String content, String image, Recipe recipe) {
        this.step = step;
        this.content = content;
        this.image = image;
        this.recipe = recipe;
    }
}
