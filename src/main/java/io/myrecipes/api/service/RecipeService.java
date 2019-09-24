package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    Recipe readRecipe(int id);
    List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);
    Recipe createRecipe(Recipe recipe);
    RecipeDTO updateRecipe(int id, Recipe recipe);
    void deleteRecipe(int id);
}
