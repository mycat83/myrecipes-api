package link.myrecipes.api.dto.request;

import link.myrecipes.api.domain.RecipeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequest {
    @NotBlank(message = "레시피 제목을 입력해주세요.")
    @Length(max = 60, message = "레시피 제목은 최대 60자까지 입력이 가능합니다.")
    private String title;

    @NotBlank(message = "레시피 이미지를 등록해주세요.")
    @Length(max = 200, message = "레시피 이미지는 최대 200자까지 입력이 가능합니다.")
    private String image;

    @NotNull(message = "소요시간을 입력해주세요.")
    @Positive(message = "소요시간을 양수로 입력해주세요.")
    private Integer estimatedTime;

    @NotNull(message = "난이도를 입력해주세요.")
    @Min(value = 1, message = "난이도는 최소 1부터 입력이 가능합니다.")
    @Max(value = 5, message = "난이도는 최대 5까지 입력이 가능합니다.")
    private Integer difficulty;

    @NotEmpty(message = "요리 재료를 1개 이상 입력해주세요.")
    private List<@Valid RecipeMaterialRequest> recipeMaterialRequestList = new ArrayList<>();

    @NotEmpty(message = "요리 순서를 1개 이상 입력해주세요.")
    private List<@Valid RecipeStepRequest> recipeStepRequestList = new ArrayList<>();

    private List<@Valid RecipeTagRequest> recipeTagRequestList = new ArrayList<>();

    @Builder
    public RecipeRequest(String title, String image, Integer estimatedTime, Integer difficulty) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
    }

    public void addRecipeMaterial(RecipeMaterialRequest recipeMaterialRequest) {
        this.recipeMaterialRequestList.add(recipeMaterialRequest);
    }

    public void addRecipeStep(RecipeStepRequest recipeStepRequest) {
        this.recipeStepRequestList.add(recipeStepRequest);
    }

    public void addRecipeTag(RecipeTagRequest recipeTagRequest) {
        this.recipeTagRequestList.add(recipeTagRequest);
    }

    public RecipeEntity toEntity() {
        return RecipeEntity.builder()
                .title(this.getTitle())
                .image(this.getImage())
                .estimatedTime(this.getEstimatedTime())
                .difficulty(this.getDifficulty())
                .build();
    }
}
