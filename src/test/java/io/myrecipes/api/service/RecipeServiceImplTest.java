package io.myrecipes.api.service;

import io.myrecipes.api.domain.RecipeEntity;
import io.myrecipes.api.dto.Recipe;
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
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        this.recipe1 = Recipe.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipe2 = Recipe.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipe3 = Recipe.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).build();
    }

    @Test
    public void Should_동일한_항목_반환_When_저장_성공() {
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(this.recipe1.toDomain());

        final Recipe savedRecipe = this.recipeService.createRecipe(this.recipe1);

        assertThat(savedRecipe, not(nullValue()));
        assertThat(savedRecipe.getTitle(), equalTo(this.recipe1.getTitle()));
        assertThat(savedRecipe.getImage(), equalTo(this.recipe1.getImage()));
        assertThat(savedRecipe.getEstimatedTime(), equalTo(this.recipe1.getEstimatedTime()));
        assertThat(savedRecipe.getDifficulty(), equalTo(this.recipe1.getDifficulty()));
    }

    @Test
    public void Should_첫번째_페이지_반환_When_0_페이지_조회() {
        List<Recipe> list = new ArrayList<>();
        list.add(this.recipe1);
        list.add(this.recipe2);
        list.add(this.recipe3);

        Page<RecipeEntity> page = new PageImpl<>(
                list.stream().map(Recipe::toDomain).collect(Collectors.toList()),
                PageRequest.of(0, list.size()), list.size()
        );
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);

        final List<Recipe> foundList = this.recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);

        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is(this.recipe1.getTitle()));
        assertThat(foundList.get(1).getTitle(), is(this.recipe2.getTitle()));
        assertThat(foundList.get(2).getTitle(), is(this.recipe3.getTitle()));
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        given(this.recipeRepository.getOne(1)).willReturn(this.recipe1.toDomain());
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(this.recipe2.toDomain());

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipe2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        given(this.recipeRepository.getOne(1)).willReturn(null);

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, is(nullValue()));
    }

    @Test
    public void Should_1_반환_When_1건_조회() {
        given(this.recipeRepository.count()).willReturn(1L);

        final long recipeCnt = this.recipeService.readRecipeCnt();

        assertThat(recipeCnt, is(1L));
    }
}