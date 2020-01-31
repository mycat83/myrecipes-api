package link.myrecipes.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RecipeTag implements Serializable {

    private String tag;

    @Builder
    public RecipeTag(String tag) {
        this.tag = tag;
    }
}
