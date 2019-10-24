package link.myrecipes.api.service;

import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.RecipeRepository;
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
    public RecipeView readRecipe(int id) {
        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(id);

        if (!recipeEntityOptional.isPresent()) {
            throw new NotExistDataException(RecipeEntity.class, id);
        }

        return recipeEntityOptional.get().toViewDTO();
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
    public Recipe createRecipe(RecipeRequest recipeRequest, int userId) {
        RecipeEntity recipeEntity = recipeRequest.toEntity();
        recipeEntity.setRegisterUserId(userId);

        for (RecipeMaterialRequest recipeMaterialRequest : recipeRequest.getRecipeMaterialRequestList()) {
            Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(recipeMaterialRequest.getMaterialId());
            if (!materialEntityOptional.isPresent()) {
                throw new NotExistDataException(MaterialEntity.class, recipeMaterialRequest.getMaterialId());
            }

            RecipeMaterialEntity recipeMaterialEntity = recipeMaterialRequest.toEntity();
            recipeMaterialEntity.setRecipeEntity(recipeEntity);
            recipeMaterialEntity.setMaterialEntity(materialEntityOptional.get());

            recipeEntity.addRecipeMaterial(recipeMaterialEntity);
        }

        for (RecipeStepRequest recipeStepRequest : recipeRequest.getRecipeStepRequestList()) {
            RecipeStepEntity recipeStepEntity = recipeStepRequest.toEntity();
            recipeStepEntity.setRecipeEntity(recipeEntity);

            recipeEntity.addRecipeStep(recipeStepEntity);
        }

        for (RecipeTagRequest recipeTagRequest: recipeRequest.getRecipeTagRequestList()) {
            RecipeTagEntity recipeTagEntity = recipeTagRequest.toEntity();
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
