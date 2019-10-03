package io.myrecipes.api.service;

import io.myrecipes.api.domain.*;
import io.myrecipes.api.dto.*;
import io.myrecipes.api.exception.NotExistDataException;
import io.myrecipes.api.repository.MaterialRepository;
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
    private final MaterialRepository materialRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,MaterialRepository materialRepository) {
        this.recipeRepository = recipeRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public Recipe readRecipe(int id) {
        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(id);

        if (!recipeEntityOptional.isPresent()) {
            throw new NotExistDataException(RecipeEntity.class, id);
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
    public Recipe createRecipe(RecipeReq recipeReq, int userId) {
        RecipeEntity recipeEntity = recipeReq.toEntity();
        recipeEntity.setRegisterUserId(userId);

        for (RecipeMaterial recipeMaterial: recipeReq.getRecipeMaterialList()) {
            Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(recipeMaterial.getMaterialId());
            if (!materialEntityOptional.isPresent()) {
                throw new NotExistDataException(MaterialEntity.class, recipeMaterial.getMaterialId());
            }

            RecipeMaterialEntity recipeMaterialEntity = recipeMaterial.toEntity();
            recipeMaterialEntity.setRecipeEntity(recipeEntity);
            recipeMaterialEntity.setMaterialEntity(materialEntityOptional.get());

            recipeEntity.addRecipeMaterial(recipeMaterialEntity);
        }

        for (RecipeStep recipeStep: recipeReq.getRecipeStepList()) {
            RecipeStepEntity recipeStepEntity = recipeStep.toEntity();
            recipeStepEntity.setRecipeEntity(recipeEntity);

            recipeEntity.addRecipeStep(recipeStepEntity);
        }

        for (RecipeTag recipeTag: recipeReq.getRecipeTagList()) {
            RecipeTagEntity recipeTagEntity = recipeTag.toEntity();
            recipeTagEntity.setRecipeEntity(recipeEntity);

            recipeEntity.addRecipeTag(recipeTagEntity);
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
