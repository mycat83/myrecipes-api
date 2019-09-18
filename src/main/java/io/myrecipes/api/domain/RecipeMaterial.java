package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipe_material")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeMaterial {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    public RecipeMaterial(Integer quantity, Recipe recipe, Material material) {
        this.quantity = quantity;
        this.recipe = recipe;
        this.material = material;
    }
}
