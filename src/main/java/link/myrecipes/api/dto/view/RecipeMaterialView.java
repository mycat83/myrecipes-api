package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RecipeMaterialView implements Serializable {
    private String materialName;

    private String materialUnitName;

    private Double quantity;

    @Builder
    public RecipeMaterialView(String materialName, String materialUnitName, Double quantity) {
        this.materialName = materialName;
        this.materialUnitName = materialUnitName;
        this.quantity = quantity;
    }
}
