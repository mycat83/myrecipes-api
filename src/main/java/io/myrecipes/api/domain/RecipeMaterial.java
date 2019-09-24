package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeMaterial {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Builder
    public RecipeMaterial(Integer quantity, Recipe recipe, Material material) {
        this.quantity = quantity;
        this.recipe = recipe;
        this.material = material;
    }
}
