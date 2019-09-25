package io.myrecipes.api.service;

import io.myrecipes.api.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    RecipeDTO readRecipe(int id);
    List<RecipeDTO> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);
    RecipeDTO createRecipe(RecipeDTO recipeDTO);
    RecipeDTO updateRecipe(int id, RecipeDTO recipeDTO);
    void deleteRecipe(int id);
}
