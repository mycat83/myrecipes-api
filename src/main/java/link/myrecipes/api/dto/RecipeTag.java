package link.myrecipes.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeTag {
    private String tag;

    @Builder
    public RecipeTag(String tag) {
        this.tag = tag;
    }
}
