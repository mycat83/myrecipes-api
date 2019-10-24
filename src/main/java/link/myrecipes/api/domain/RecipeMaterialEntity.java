package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import link.myrecipes.api.dto.view.RecipeMaterialView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeMaterialEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Double quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private MaterialEntity materialEntity;

    @Builder
    public RecipeMaterialEntity(Double quantity, RecipeEntity recipeEntity, MaterialEntity materialEntity) {
        this.quantity = quantity;
        this.recipeEntity = recipeEntity;
        this.materialEntity = materialEntity;
    }

    public void setRecipeEntity(RecipeEntity recipeEntity) {
        this.recipeEntity = recipeEntity;
    }

    public void setMaterialEntity(MaterialEntity materialEntity) {
        this.materialEntity = materialEntity;
    }

    public RecipeMaterialView toViewDTO() {
        return RecipeMaterialView.builder()
                .materialName(this.getMaterialEntity().getName())
                .materialUnitName(this.getMaterialEntity().getUnitEntity().getName())
                .quantity(this.getQuantity())
                .build();
    }
}
