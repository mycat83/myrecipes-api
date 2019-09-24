package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeTag {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String tag;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Builder
    public RecipeTag(String tag, Recipe recipe) {
        this.tag = tag;
        this.recipe = recipe;
    }
}
