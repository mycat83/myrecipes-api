package link.myrecipes.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RecipeTagRequest {

    @NotBlank(message = "태그를 정확히 입력해주세요.")
    @Length(max = 20, message = "태그는 건당 최대 20자까지 입력이 가능합니다.")
    private String tag;

    @Builder
    public RecipeTagRequest(String tag) {
        this.tag = tag;
    }
}
