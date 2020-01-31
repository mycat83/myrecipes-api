package link.myrecipes.api.service;

import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeCount;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMaterialRepository recipeMaterialRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeTagRepository recipeTagRepository;
    private final MaterialRepository materialRepository;
    private final PopularRecipeRepository popularRecipesRepository;
    private final ModelMapper modelMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMaterialRepository recipeMaterialRepository,
                             RecipeStepRepository recipeStepRepository, RecipeTagRepository recipeTagRepository,
                             MaterialRepository materialRepository, PopularRecipeRepository popularRecipesRepository,
                             ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMaterialRepository = recipeMaterialRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.recipeTagRepository = recipeTagRepository;
        this.materialRepository = materialRepository;
        this.popularRecipesRepository = popularRecipesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Cacheable(value = "myrecipe:api:recipeView", key = "#id")
    public RecipeView readRecipe(int id) {
        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(id);

        if (recipeEntityOptional.isEmpty()) {
            throw new NotExistDataException(RecipeEntity.class, id);
        }

        return this.modelMapper.map(recipeEntityOptional.get(), RecipeView.class);
    }

    @Override
    @Cacheable(value = "myrecipe:api:recipeList", key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public Page<Recipe> readRecipeList(Pageable pageable) {

        return this.recipeRepository.findAll(pageable)
                .map(recipeEntity -> modelMapper.map(recipeEntity, Recipe.class));
    }

    @Override
    @Transactional
    @CacheEvict(value = "myrecipe:api:recipeList", allEntries = true)
    public Recipe createRecipe(RecipeRequest recipeRequest, int userId) {

        RecipeEntity recipeEntity = this.modelMapper.map(recipeRequest, RecipeEntity.class);
        recipeEntity.setRegisterUserId(userId);

        return saveRecipe(recipeRequest, recipeEntity);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "myrecipe:api:recipeView", key = "#id"),
            @CacheEvict(value = "myrecipe:api:recipeList", allEntries = true)
    })
    public Recipe updateRecipe(int id, RecipeRequest recipeRequest, int userId) {

        Optional<RecipeEntity> recipeOptional = this.recipeRepository.findById(id);

        if (recipeOptional.isEmpty()) {
            throw new NotExistDataException(RecipeEntity.class, id);
        }

        RecipeEntity readRecipeEntity = recipeOptional.get();
        RecipeEntity recipeEntity = this.modelMapper.map(recipeRequest, RecipeEntity.class);
        readRecipeEntity.update(recipeEntity, userId);

        for (RecipeMaterialEntity recipeMaterialEntity : readRecipeEntity.getRecipeMaterialEntityList()) {
            this.recipeMaterialRepository.delete(recipeMaterialEntity);
        }
        for (RecipeStepEntity recipeStepEntity : readRecipeEntity.getRecipeStepEntityList()) {
            this.recipeStepRepository.delete(recipeStepEntity);
        }
        for (RecipeTagEntity recipeTagEntity : readRecipeEntity.getRecipeTagEntityList()) {
            this.recipeTagRepository.delete(recipeTagEntity);
        }
        readRecipeEntity.clearRecipeMaterialEntityList();
        readRecipeEntity.clearRecipeStepEntityList();
        readRecipeEntity.clearRecipeTagEntityList();

        return saveRecipe(recipeRequest, readRecipeEntity);
    }

    private Recipe saveRecipe(RecipeRequest recipeRequest, RecipeEntity recipeEntity) {

        for (RecipeMaterialRequest recipeMaterialRequest : recipeRequest.getRecipeMaterialRequestList()) {
            Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(recipeMaterialRequest.getMaterialId());
            if (materialEntityOptional.isEmpty()) {
                throw new NotExistDataException(MaterialEntity.class, recipeMaterialRequest.getMaterialId());
            }

            RecipeMaterialEntity recipeMaterialEntity = RecipeMaterialEntity.builder()
                    .quantity(recipeMaterialRequest.getQuantity())
                    .build();
            recipeMaterialEntity.setRecipeEntity(recipeEntity);
            recipeMaterialEntity.setMaterialEntity(materialEntityOptional.get());

            recipeEntity.addRecipeMaterial(recipeMaterialEntity);
        }

        for (RecipeStepRequest recipeStepRequest : recipeRequest.getRecipeStepRequestList()) {
            RecipeStepEntity recipeStepEntity = this.modelMapper.map(recipeStepRequest, RecipeStepEntity.class);
            recipeStepEntity.setRecipeEntity(recipeEntity);

            recipeEntity.addRecipeStep(recipeStepEntity);
        }

        for (RecipeTagRequest recipeTagRequest : recipeRequest.getRecipeTagRequestList()) {
            RecipeTagEntity recipeTagEntity = this.modelMapper.map(recipeTagRequest, RecipeTagEntity.class);
            recipeTagEntity.setRecipeEntity(recipeEntity);

            recipeEntity.addRecipeTag(recipeTagEntity);
        }

        RecipeEntity savedRecipeEntity = this.recipeRepository.save(recipeEntity);
        return this.modelMapper.map(savedRecipeEntity, Recipe.class);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "myrecipe:api:recipeView", key = "#id"),
            @CacheEvict(value = "myrecipe:api:recipeList", allEntries = true)
    })
    public void deleteRecipe(int id) {
        this.recipeRepository.deleteById(id);
    }

    @Override
    public RecipeCount readRecipeCount() {
        long count = this.recipeRepository.count();
        return new RecipeCount(count);
    }

    @Override
    @Transactional
    public void increaseReadCount(int id) {
        this.recipeRepository.increaseReadCount(id);
    }

    @Override
    public List<Recipe> readPopularRecipeList() {
        List<PopularRecipeDocument> popularRecipeDocumentList = this.popularRecipesRepository.findAllByOrderBySequence();
        return popularRecipeDocumentList.stream().map(PopularRecipeDocument::toDTO).collect(Collectors.toList());
    }
}
