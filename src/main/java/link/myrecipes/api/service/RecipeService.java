package link.myrecipes.api.service;

import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;

import java.util.List;

public interface RecipeService {
    RecipeView readRecipe(int id);

    List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending);

    Recipe createRecipe(RecipeRequest recipe, int userId);

    Recipe updateRecipe(int id, RecipeRequest recipe, int userId);

    void deleteRecipe(int id);

    long readRecipeCount();

    void increaseReadCount(int id);

    List<Recipe> readPopularRecipeList();
}
