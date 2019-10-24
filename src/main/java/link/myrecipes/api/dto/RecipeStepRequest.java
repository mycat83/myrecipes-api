package link.myrecipes.api.dto;

import link.myrecipes.api.domain.RecipeStepEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class RecipeStepRequest {
    @NotNull(message = "단계를 입력해주세요.")
    @Positive(message = "단계를 양수로 입력해주세요.")
    private Integer step;

    @NotBlank(message = "요리 순서의 내용을 입력해주세요.")
    private String content;

    @Length(max = 200, message = "요리 순서 이미지는 최대 200자까지 입력이 가능합니다.")
    private String image;

    @Builder
    public RecipeStepRequest(Integer step, String content, String image) {
        this.step = step;
        this.content = content;
        this.image = image;
    }

    public RecipeStepEntity toEntity() {
        return RecipeStepEntity.builder()
                .step(this.getStep())
                .content(this.getContent())
                .image(this.getImage())
                .build();
    }
}
