package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe readRecipe(int id);
    List<Recipe> readRecipeList();
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(int id, Recipe recipe);
    void deleteRecipe(int id);
}
