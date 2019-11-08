package link.myrecipes.api.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RecipeTagView implements Serializable {
    private String tag;

    @Builder
    public RecipeTagView(String tag) {
        this.tag = tag;
    }
}
