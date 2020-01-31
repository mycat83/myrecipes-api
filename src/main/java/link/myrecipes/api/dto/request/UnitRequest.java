package link.myrecipes.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UnitRequest {

    @NotBlank(message = "단위 이름을 입력해주세요.")
    private String name;

    private String exchangeUnitName;

    private Double exchangeQuantity;

    @Builder
    public UnitRequest(String name, String exchangeUnitName, Double exchangeQuantity) {

        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
    }
}
