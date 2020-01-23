package link.myrecipes.api.service;

import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeCount;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    RecipeView readRecipe(int id);

    Page<Recipe> readRecipeList(Pageable pageable);

    Recipe createRecipe(RecipeRequest recipe, int userId);

    Recipe updateRecipe(int id, RecipeRequest recipe, int userId);

    void deleteRecipe(int id);

    RecipeCount readRecipeCount();

    void increaseReadCount(int id);

    List<Recipe> readPopularRecipeList();
}
