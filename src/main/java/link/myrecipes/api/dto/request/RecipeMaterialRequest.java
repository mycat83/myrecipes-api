package link.myrecipes.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class RecipeMaterialRequest {

    @Positive(message = "요리 재료를 입력해주세요.")
    private Integer materialId;

    @NotNull(message = "요리 재료의 수량을 입력해주세요.")
    @Positive(message = "요리 재료의 수량을 양수로 입력해주세요.")
    private Double quantity;

    @Builder
    public RecipeMaterialRequest(Integer materialId, Double quantity) {
        this.materialId = materialId;
        this.quantity = quantity;
    }
}
