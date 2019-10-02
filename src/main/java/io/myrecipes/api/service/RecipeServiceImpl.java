package io.myrecipes.api.service;

import io.myrecipes.api.domain.RecipeEntity;
import io.myrecipes.api.domain.RecipeMaterialEntity;
import io.myrecipes.api.dto.*;
import io.myrecipes.api.exception.NotExistDataException;
import io.myrecipes.api.repository.RecipeMaterialRepository;
import io.myrecipes.api.repository.RecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMaterialRepository recipeMaterialRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMaterialRepository recipeMaterialRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMaterialRepository = recipeMaterialRepository;
    }

    @Override
    public Recipe readRecipe(int id) {
        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(id);

        if (!recipeEntityOptional.isPresent()) {
            throw new NotExistDataException("RecipeEntity", id);
        }

        return recipeEntityOptional.get().toDTO();
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
    @Transactional
    public Recipe createRecipe(RecipeReq recipeReq) {
        RecipeEntity recipeEntity = recipeReq.toEntity();

        for (RecipeMaterial recipeMaterial: recipeReq.getRecipeMaterialList()) {
            Optional<RecipeMaterialEntity> recipeMaterialEntityOptional = this.recipeMaterialRepository.findById(recipeMaterial.getMaterialId());
            if (!recipeMaterialEntityOptional.isPresent()) {
                throw new NotExistDataException("RecipeMaterialEntity", recipeMaterial.getMaterialId());
            }

            recipeEntity.addRecipeMaterial(recipeMaterialEntityOptional.get());
        }

        for (RecipeStep recipeStep: recipeReq.getRecipeStepList()) {
            recipeEntity.addRecipeStep(recipeStep.toEntity());
        }

        for (RecipeTag recipeTag: recipeReq.getRecipeTagList()) {
            recipeEntity.addRecipeTag(recipeTag.toEntity());
        }

        this.recipeRepository.save(recipeEntity);
        return recipeEntity.toDTO();
    }

    @Override
    public Recipe updateRecipe(int id, Recipe recipe) {
        Optional<RecipeEntity> recipeOptional = this.recipeRepository.findById(id);

        if (!recipeOptional.isPresent()) {
            return null;
        }

        RecipeEntity selectedRecipeEntity = recipeOptional.get();
        selectedRecipeEntity.update(recipe.toEntity());

        return this.recipeRepository.save(selectedRecipeEntity).toDTO();
    }

    @Override
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }

    @Override
    public long readRecipeCnt() {
        return this.recipeRepository.count();
    }
}
