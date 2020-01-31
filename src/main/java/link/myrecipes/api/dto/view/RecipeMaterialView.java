package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RecipeMaterialView implements Serializable {

    private Integer materialId;

    private String materialName;

    private String materialUnitName;

    private Double quantity;

    @Builder
    public RecipeMaterialView(Integer materialId, String materialName, String materialUnitName, Double quantity) {

        this.materialId = materialId;
        this.materialName = materialName;
        this.materialUnitName = materialUnitName;
        this.quantity = quantity;
    }
}
