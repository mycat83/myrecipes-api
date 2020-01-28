package link.myrecipes.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MaterialRequest {

    @NotBlank(message = "재료 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "재료 단위를 입력해주세요.")   // TODO: 재료 단위 존재여부 Custom Validator 적용
    private String unitName;

    @Builder
    public MaterialRequest(String name, String unitName) {

        this.name = name;
        this.unitName = unitName;
    }
}
