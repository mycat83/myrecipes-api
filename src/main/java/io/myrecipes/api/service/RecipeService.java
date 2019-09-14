package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Recipe readRecipe(int id);
    Page<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(int id, Recipe recipe);
    void deleteRecipe(int id);
}
