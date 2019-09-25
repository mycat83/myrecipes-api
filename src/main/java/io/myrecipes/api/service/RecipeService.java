package io.myrecipes.api.service;

import io.myrecipes.api.dto.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe readRecipe(int id);
    List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(int id, Recipe recipe);
    void deleteRecipe(int id);
}
