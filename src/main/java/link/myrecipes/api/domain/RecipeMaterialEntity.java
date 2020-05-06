package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import link.myrecipes.api.dto.view.RecipeMaterialView;
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
@Table(name = "recipe_material")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeMaterialEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
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
                .materialId(this.getMaterialEntity() == null ? 0 : this.getMaterialEntity().getId())
                .materialName(this.getMaterialEntity() == null ? "" : this.getMaterialEntity().getName())
                .materialUnitName(this.getMaterialEntity() == null ? "" : this.getMaterialEntity().getUnitEntity().getName())
                .quantity(this.getQuantity())
                .build();
    }
}
