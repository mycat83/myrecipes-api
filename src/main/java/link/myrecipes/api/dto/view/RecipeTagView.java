package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeTagView {
    private String tag;

    @Builder
    public RecipeTagView(String tag) {
        this.tag = tag;
    }
}
