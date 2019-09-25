package io.myrecipes.api.service;

import io.myrecipes.api.domain.RecipeEntity;
import io.myrecipes.api.dto.Recipe;
import io.myrecipes.api.repository.RecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe readRecipe(int id) {
        return this.recipeRepository.getOne(id).toDTO();
    }

    @Override
    public List<Recipe> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending) {
        PageRequest pageable;
        if (isDescending) {
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortField);
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortField);
        }

        return this.recipeRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(RecipeEntity::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe.toDomain()).toDTO();
    }

    @Override
    public Recipe updateRecipe(int id, Recipe recipe) {
        Optional<RecipeEntity> recipeOptional = Optional.ofNullable(this.recipeRepository.getOne(id));

        if (!recipeOptional.isPresent()) {
            return null;
        }

        RecipeEntity selectedRecipeEntity = recipeOptional.get();
        selectedRecipeEntity.update(recipe.toDomain());

        return this.recipeRepository.save(selectedRecipeEntity).toDTO();
    }

    @Override
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }
}
