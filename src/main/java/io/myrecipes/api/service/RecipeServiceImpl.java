package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.dto.RecipeDTO;
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
    public RecipeDTO readRecipe(int id) {
        return this.recipeRepository.getOne(id).toDTO();
    }

    @Override
    public List<RecipeDTO> readRecipePageSortedByParam(int page, int size, String sortField, boolean isDescending) {
        PageRequest pageable;
        if (isDescending) {
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortField);
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortField);
        }

        return this.recipeRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(Recipe::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        return this.recipeRepository.save(recipeDTO.toDomain()).toDTO();
    }

    @Override
    public RecipeDTO updateRecipe(int id, RecipeDTO recipeDTO) {
        Optional<Recipe> recipeOptional = Optional.ofNullable(this.recipeRepository.getOne(id));

        if (!recipeOptional.isPresent()) {
            return null;
        }

        Recipe selectedRecipe = recipeOptional.get();
        selectedRecipe.update(recipeDTO.toDomain());

        return this.recipeRepository.save(selectedRecipe).toDTO();
    }

    @Override
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }
}
