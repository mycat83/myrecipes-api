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
        this.recipe1 = Recipe.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).registerUserId(1001).build();
        this.recipe2 = Recipe.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).registerUserId(1002).build();
        this.recipe3 = Recipe.builder().title("test2").image("image3.jpg").estimatedTime(90).difficulty(5).registerUserId(1003).build();
    }

    @Test
    public void Should_동일한_엔티티_반환_When_엔티티_저장() {
        this.recipeRepository.save(this.recipe1);
        final List<Recipe> recipeList = this.recipeRepository.findAll();

        assertThat(recipeList.size(), is(1));
        assertThat(recipeList.get(0).getTitle(), is(this.recipe1.getTitle()));
        assertThat(recipeList.get(0).getImage(), is(this.recipe1.getImage()));
        assertThat(recipeList.get(0).getEstimatedTime(), is(this.recipe1.getEstimatedTime()));
        assertThat(recipeList.get(0).getDifficulty(), is(this.recipe1.getDifficulty()));
        assertThat(recipeList.get(0).getRegisterUserId(), is(this.recipe1.getRegisterUserId()));

        this.recipe1.update(this.recipe2);
        this.recipeRepository.save(this.recipe1);
        final List<Recipe> updatedRecipeList = this.recipeRepository.findAll();

        assertThat(updatedRecipeList.size(), is(1));
        assertThat(updatedRecipeList.get(0).getTitle(), is(this.recipe2.getTitle()));
        assertThat(updatedRecipeList.get(0).getImage(), is(this.recipe2.getImage()));
        assertThat(updatedRecipeList.get(0).getEstimatedTime(), is(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipeList.get(0).getDifficulty(), is(this.recipe2.getDifficulty()));
        assertThat(updatedRecipeList.get(0).getModifyUserId(), is(this.recipe2.getModifyUserId()));
    }

    @Test
    public void Should_키_순차적_증가_When_엔티티_여러개_저장() {
        recipeRepository.save(this.recipe1);
        recipeRepository.save(this.recipe2);
        recipeRepository.save(this.recipe3);
        final List<Recipe> recipeList = this.recipeRepository.findAll();

        assertThat(recipeList.size(), is(3));
        assertThat(recipeList.get(0).getId(), is(1));
        assertThat(recipeList.get(1).getId(), is(2));
        assertThat(recipeList.get(2).getId(), is(3));
    }

    @Test
    public void Should_엔티티_없음_When_엔티티_저장후_삭제() {
        this.recipeRepository.save(this.recipe1);
        this.recipeRepository.deleteById(this.recipe1.getId());
        final Optional<Recipe> recipeOptional = this.recipeRepository.findById(this.recipe1.getId());

        assertThat(recipeOptional.isPresent(), is(false));
    }

    @Test
    public void Should_연관관계_정상_조회_When_연관관계_매핑() {
        this.recipeRepository.save(this.recipe1);

        List<Unit> unitList = new ArrayList<>();
        unitList.add(Unit.builder().name("g").build());
        unitList.add(Unit.builder().name("kg").exchangeUnitName("g").registerUserId(1000).build());
        this.unitRepository.saveAll(unitList);

        List<Material> materialList = new ArrayList<>();
        materialList.add(Material.builder().name("material1").registerUserId(1001).unit(unitList.get(0)).build());
        materialList.add(Material.builder().name("material2").registerUserId(1002).unit(unitList.get(1)).build());
        materialList.add(Material.builder().name("material3").registerUserId(1003).unit(unitList.get(0)).build());
        this.materialRepository.saveAll(materialList);

        List<RecipeMaterial> recipeMaterialList = new ArrayList<>();
        recipeMaterialList.add(RecipeMaterial.builder().quantity(10).recipe(this.recipe1).material(materialList.get(0)).build());
        recipeMaterialList.add(RecipeMaterial.builder().quantity(20).recipe(this.recipe1).material(materialList.get(1)).build());
        recipeMaterialList.add(RecipeMaterial.builder().quantity(30).recipe(this.recipe1).material(materialList.get(2)).build());
        recipeMaterialList.get(0).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(0));
        recipeMaterialList.get(1).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(1));
        recipeMaterialList.get(2).getRecipe().getRecipeMaterialList().add(recipeMaterialList.get(2));
        this.recipeMaterialRepository.saveAll(recipeMaterialList);

        List<RecipeStep> recipeStepList = new ArrayList<>();
        recipeStepList.add(RecipeStep.builder().step(1).content("step1").image("step1.jpg").recipe(this.recipe1).build());
        recipeStepList.add(RecipeStep.builder().step(1).content("step2").image("step2.jpg").recipe(this.recipe1).build());
        recipeStepList.get(0).getRecipe().getRecipeStepList().add(recipeStepList.get(0));
        recipeStepList.get(1).getRecipe().getRecipeStepList().add(recipeStepList.get(1));
        this.recipeStepRepository.saveAll(recipeStepList);

        RecipeTag recipeTag = RecipeTag.builder().tag("tag1").recipe(this.recipe1).build();
        recipeTag.getRecipe().getRecipeTagList().add(recipeTag);
        this.recipeTagRepository.save(recipeTag);

        final Recipe savedRecipe = this.recipeRepository.getOne(this.recipe1.getId());

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