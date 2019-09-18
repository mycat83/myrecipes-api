package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.repository.RecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe readRecipe(int id) {
        return this.recipeRepository.getOne(id);
    }

    @Override
    public List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending) {
        PageRequest pageable;
        if (isDescending) {
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortField);
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortField);
        }

        return this.recipeRepository.findAll(pageable).getContent();
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(int id, Recipe recipe) {
        Optional<Recipe> recipeOptional = Optional.ofNullable(this.recipeRepository.getOne(id));

        if (!recipeOptional.isPresent()) {
            return null;
        }

        Recipe selectedRecipe = recipeOptional.get();
        selectedRecipe.setTitle(recipe.getTitle());
        selectedRecipe.setImage(recipe.getImage());
        selectedRecipe.setEstimatedTime(recipe.getEstimatedTime());
        selectedRecipe.setDifficulty(recipe.getDifficulty());
        selectedRecipe.setModifyUserId(recipe.getModifyUserId());
        return this.recipeRepository.save(selectedRecipe);
    }

    @Override
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }
}
