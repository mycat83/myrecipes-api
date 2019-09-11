package io.myrecipes.api.repository;

import io.myrecipes.api.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMaterialRepository recipeMaterialRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @Before
    public void setUp() {
        recipe1 = new Recipe("test1", "test1.jpg", 30, "1", 1001);
        recipe2 = new Recipe("test2", "test2.jpg", 60, "2", 1002);
        recipe3 = new Recipe("test3", "test3.jpg", 90, "3", 1003);
    }

    @Test
    public void Should_동일한_엔티티_반환_When_엔티티_저장() {
        recipeRepository.save(recipe1);
        List<Recipe> recipeList = recipeRepository.findAll();

        assertThat(recipeList.size(), is(1));
        assertThat(recipeList.get(0).getTitle(), is("test1"));
        assertThat(recipeList.get(0).getImage(), is("test1.jpg"));
        assertThat(recipeList.get(0).getEstimatedTime(), is(30));
        assertThat(recipeList.get(0).getDifficulty(), is("1"));
        assertThat(recipeList.get(0).getRegisterUserId(), is(1001));

        recipe1.setTitle(recipe2.getTitle());
        recipe1.setImage(recipe2.getImage());
        recipe1.setEstimatedTime(recipe2.getEstimatedTime());
        recipe1.setDifficulty(recipe2.getDifficulty());
        recipe1.setModifyUserId(recipe2.getRegisterUserId());
        recipeRepository.save(recipe1);
        final List<Recipe> updatedRecipeList = recipeRepository.findAll();

        assertThat(updatedRecipeList.size(), is(1));
        assertThat(updatedRecipeList.get(0).getTitle(), is(recipe2.getTitle()));
        assertThat(updatedRecipeList.get(0).getImage(), is(recipe2.getImage()));
        assertThat(updatedRecipeList.get(0).getEstimatedTime(), is(recipe2.getEstimatedTime()));
        assertThat(updatedRecipeList.get(0).getDifficulty(), is(recipe2.getDifficulty()));
        assertThat(updatedRecipeList.get(0).getModifyUserId(), is(recipe2.getRegisterUserId()));
    }

    @Test
    public void Should_키_순차적_증가_When_엔티티_여러개_저장() {
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
        recipeRepository.save(recipe3);
        final List<Recipe> recipeList = recipeRepository.findAll();

        assertThat(recipeList.size(), is(3));
        assertThat(recipeList.get(0).getId(), is(1));
        assertThat(recipeList.get(1).getId(), is(2));
        assertThat(recipeList.get(2).getId(), is(3));
    }

    @Test
    public void Should_엔티티_없음_When_엔티티_저장후_삭제() {
        recipeRepository.save(recipe1);
        recipeRepository.deleteById(recipe1.getId());
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipe1.getId());

        assertThat(recipeOptional.isPresent(), is(false));
    }

    @Test
    public void Should_연관관계_정상_조회_When_연관관계_매핑() {
        recipeRepository.save(recipe1);

        List<Unit> unitList = new ArrayList<>();
        unitList.add(new Unit("g"));
        unitList.add(new Unit("kg", "g", 1000, 1001));
        unitRepository.saveAll(unitList);

        List<Material> materialList = new ArrayList<>();
        materialList.add(new Material("material1", 1001, unitList.get(0)));
        materialList.add(new Material("material2", 1002, unitList.get(1)));
        materialList.add(new Material("material3", 1003, unitList.get(0)));
        materialRepository.saveAll(materialList);

        List<RecipeMaterial> recipeMaterialList = new ArrayList<>();
        recipeMaterialList.add(new RecipeMaterial(10, recipe1, materialList.get(0)));
        recipeMaterialList.add(new RecipeMaterial(20, recipe1, materialList.get(1)));
        recipeMaterialList.add(new RecipeMaterial(30, recipe1, materialList.get(2)));
        recipeMaterialList.get(0).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(0));
        recipeMaterialList.get(1).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(1));
        recipeMaterialList.get(2).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(2));
        recipeMaterialRepository.saveAll(recipeMaterialList);

        List<RecipeStep> recipeStepList = new ArrayList<>();
        recipeStepList.add(new RecipeStep(1, "step1", "step1.jpg", recipe1));
        recipeStepList.add(new RecipeStep(2, "step2", "step2.jpg", recipe1));
        recipeStepList.get(0).getRecipe().getRecipeStepList().add(recipeStepList.get(0));
        recipeStepList.get(1).getRecipe().getRecipeStepList().add(recipeStepList.get(1));
        recipeStepRepository.saveAll(recipeStepList);

        RecipeTag recipeTag = new RecipeTag("tag1", recipe1);
        recipeTag.getRecipe().getRecipeTagList().add(recipeTag);
        recipeTagRepository.save(recipeTag);

        final Recipe savedRecipe = recipeRepository.getOne(recipe1.getId());

        assertThat(savedRecipe.getRecipeMaterialList().size(), is(3));
        assertThat(savedRecipe.getRecipeMaterialList().get(0).getQuantity(), is(10));
        assertThat(savedRecipe.getRecipeMaterialList().get(1).getQuantity(), is(20));
        assertThat(savedRecipe.getRecipeMaterialList().get(2).getQuantity(), is(30));

        assertThat(savedRecipe.getRecipeMaterialList().get(0).getMaterial().getName(), is("material1"));
        assertThat(savedRecipe.getRecipeMaterialList().get(1).getMaterial().getName(), is("material2"));
        assertThat(savedRecipe.getRecipeMaterialList().get(2).getMaterial().getName(), is("material3"));

        assertThat(savedRecipe.getRecipeMaterialList().get(0).getMaterial().getUnit().getName(), is("g"));
        assertThat(savedRecipe.getRecipeMaterialList().get(1).getMaterial().getUnit().getName(), is("kg"));
        assertThat(savedRecipe.getRecipeMaterialList().get(2).getMaterial().getUnit().getName(), is("g"));

        assertThat(savedRecipe.getRecipeStepList().size(), is(2));
        assertThat(savedRecipe.getRecipeStepList().get(0).getContent(), is("step1"));
        assertThat(savedRecipe.getRecipeStepList().get(1).getContent(), is("step2"));

        assertThat(savedRecipe.getRecipeTagList().size(), is(1));
        assertThat(savedRecipe.getRecipeTagList().get(0).getTag(), is("tag1"));
    }
}