package io.myrecipes.api.service;

import io.myrecipes.api.domain.MaterialEntity;
import io.myrecipes.api.domain.RecipeEntity;
import io.myrecipes.api.dto.*;
import io.myrecipes.api.exception.NotExistDataException;
import io.myrecipes.api.repository.MaterialRepository;
import io.myrecipes.api.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private RecipeRequest recipeRequest;

    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Before
    public void setUp() {
        RecipeMaterial recipeMaterial = RecipeMaterial.builder().materialId(1).quantity(5).build();

        RecipeStep recipeStep = RecipeStep.builder().step(1).content("step1").image("step1.jpg").build();

        RecipeTag recipeTag1 = RecipeTag.builder().tag("tag1").build();
        RecipeTag recipeTag2 = RecipeTag.builder().tag("tag2").build();

        this.recipeRequest = RecipeRequest.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipeRequest.addRecipeMaterial(recipeMaterial);
        this.recipeRequest.addRecipeStep(recipeStep);
        this.recipeRequest.addRecipeTag(recipeTag1);
        this.recipeRequest.addRecipeTag(recipeTag2);

        this.recipe1 = Recipe.builder()
                .title(recipeRequest.getTitle()).image(recipeRequest.getImage())
                .estimatedTime(recipeRequest.getEstimatedTime()).difficulty(recipeRequest.getDifficulty())
                .build();
        this.recipe2 = Recipe.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipe3 = Recipe.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).build();
    }

    @Test
    public void Should_첫번째_페이지_반환_When_0_페이지_조회() {
        List<Recipe> list = new ArrayList<>();
        list.add(this.recipe1);
        list.add(this.recipe2);
        list.add(this.recipe3);

        Page<RecipeEntity> page = new PageImpl<>(
            list.stream().map(Recipe::toEntity).collect(Collectors.toList()),
            PageRequest.of(0, list.size()), list.size()
        );
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);

        final List<Recipe> foundList = this.recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);

        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is(this.recipe1.getTitle()));
        assertThat(foundList.get(1).getTitle(), is(this.recipe2.getTitle()));
        assertThat(foundList.get(2).getTitle(), is(this.recipe3.getTitle()));
    }

    @Test(expected = NotExistDataException.class)
    public void Should_예외_발생_When_존재하지_않는_ID_조회() {
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        this.recipeService.readRecipe(1);
    }

    @Test
    public void Should_정상_저장_확인_When_레시피_저장() {
        MaterialEntity materialEntity = MaterialEntity.builder().name("material1").build();

        Optional<MaterialEntity> materialEntityOptional = Optional.ofNullable(materialEntity);

        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(recipe1.toEntity());

        final Recipe recipe = this.recipeService.createRecipe(recipeRequest, 10001);

        assertThat(recipe, instanceOf(Recipe.class));
        assertThat(recipe.getTitle(), is(recipeRequest.getTitle()));
        assertThat(recipe.getImage(), is(recipeRequest.getImage()));
        assertThat(recipe.getEstimatedTime(), is(recipeRequest.getEstimatedTime()));
        assertThat(recipe.getDifficulty(), is(recipeRequest.getDifficulty()));
        assertThat(recipe.getRecipeTagList().size(), is(recipeRequest.getRecipeTagList().size()));
        assertThat(recipe.getRecipeTagList().get(0).getTag(), is(recipeRequest.getRecipeTagList().get(0).getTag()));
        assertThat(recipe.getRecipeTagList().get(1).getTag(), is(recipeRequest.getRecipeTagList().get(1).getTag()));
    }

    @Test(expected = NotExistDataException.class)
    public void Should_예외_발생_When_존재하지_않는_재료로_레시피_저장() {
        Optional<MaterialEntity> materialEntityOptional = Optional.empty();

        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);

        this.recipeService.createRecipe(recipeRequest, 10001);
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        given(this.recipeRepository.findById(1)).willReturn(Optional.ofNullable(this.recipe1.toEntity()));
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(this.recipe2.toEntity());

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipe2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, is(nullValue()));
    }

    @Test
    public void Should_카운트_1_반환_When_1건_조회() {
        given(this.recipeRepository.count()).willReturn(1L);

        final long recipeCnt = this.recipeService.readRecipeCnt();

        assertThat(recipeCnt, is(1L));
    }
}