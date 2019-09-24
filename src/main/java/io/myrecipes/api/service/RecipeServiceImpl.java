package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.dto.RecipeDTO;
import io.myrecipes.api.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
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
    public RecipeDTO updateRecipe(int id, Recipe recipe) {
        Optional<Recipe> recipeOptional = Optional.ofNullable(this.recipeRepository.getOne(id));

        if (!recipeOptional.isPresent()) {
            return null;
        }

        Recipe selectedRecipe = recipeOptional.get();
        selectedRecipe.update(recipe);
        Recipe updatedRecipe = this.recipeRepository.save(selectedRecipe);

        return updatedRecipe.toDTO();
    }

    @Override
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }
}
