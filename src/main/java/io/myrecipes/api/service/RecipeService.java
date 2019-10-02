package io.myrecipes.api.service;

import io.myrecipes.api.dto.Recipe;
import io.myrecipes.api.dto.RecipeReq;

import java.util.List;

public interface RecipeService {
    Recipe readRecipe(int id);

    List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);

    Recipe createRecipe(RecipeReq recipe);

    Recipe updateRecipe(int id, Recipe recipe);

    void deleteRecipe(int id);

    long readRecipeCnt();
}
