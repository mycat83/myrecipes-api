package link.myrecipes.api.dto;

import lombok.Getter;

@Getter
public class RecipeCount {

    private long count;

    public RecipeCount(long count) {
        this.count = count;
    }
}
